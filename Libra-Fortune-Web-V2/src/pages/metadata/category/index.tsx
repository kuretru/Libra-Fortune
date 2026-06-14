import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormItem,
  ProFormSelect,
  ProFormText,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { Button, Form, message, Popconfirm, Space, Tag } from 'antd';
import React, { useEffect, useMemo, useRef, useState } from 'react';
import {
  create,
  list,
  remove,
  update,
} from '@/services/libra-fortune/metadata/category';
import IconPicker, { getAntIcon } from '@/components/IconPicker';

const collectExpandableRowKeys = (
  records: LibraFortune.Metadata.CategoryDTO[],
): Set<React.Key> =>
  records.reduce<Set<React.Key>>((keys, record) => {
    if (record.id && record.children?.length) {
      keys.add(record.id);
    }

    for (const key of collectExpandableRowKeys(record.children ?? [])) {
      keys.add(key);
    }

    return keys;
  }, new Set<React.Key>());

const MetadataCategory: React.FC = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const [modalVisible, setModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<
    LibraFortune.Metadata.CategoryDTO | undefined
  >(undefined);
  const [topLevelCategories, setTopLevelCategories] = useState<
    LibraFortune.Metadata.CategoryDTO[]
  >([]);
  const [expandedRowKeys, setExpandedRowKeys] = useState<React.Key[]>([]);
  const [form] = Form.useForm<LibraFortune.Metadata.CategoryDTO>();
  const selectedParentId = Form.useWatch('parentId', form);
  const actionRef = useRef<ActionType | null>(null);
  const isChildCategory = !!(currentRecord?.parentId ?? selectedParentId);

  const parentOptions = useMemo(
    () =>
      topLevelCategories.map((category) => ({
        label: category.name,
        value: category.id,
      })),
    [topLevelCategories],
  );

  useEffect(() => {
    if (!modalVisible) return;

    if (currentRecord) {
      form.setFieldsValue(currentRecord);
    } else {
      form.resetFields();
    }
  }, [modalVisible, currentRecord, form]);

  useEffect(() => {
    if (!modalVisible || !isChildCategory) return;

    form.setFieldValue('icon', undefined);
  }, [modalVisible, isChildCategory, form]);

  const columns: ProColumns<LibraFortune.Metadata.CategoryDTO>[] = [
    {
      dataIndex: 'id',
      title: 'ID',
      valueType: 'indexBorder',
      width: 72,
    },
    {
      dataIndex: 'parentId',
      title: '层级',
      search: false,
      width: 120,
      render: (_, record) =>
        record.parentId ? (
          <Tag color="blue">二级分类</Tag>
        ) : (
          <Tag>一级分类</Tag>
        ),
    },
    {
      dataIndex: 'name',
      title: '分类名称',
      copyable: true,
      search: false,
    },
    {
      dataIndex: 'icon',
      title: '图标',
      search: false,
      width: 220,
      render: (_, record) => {
        const Icon = getAntIcon(record.icon);
        return record.icon ? (
          <Space size="small">
            {Icon && <Icon />}
            <Tag>{record.icon}</Tag>
          </Space>
        ) : (
          <span>-</span>
        );
      },
    },
    {
      key: 'action',
      title: '操作',
      align: 'right',
      fixed: 'right',
      valueType: 'option',
      width: 300,
      render: (_, record) => (
        <Space style={{ justifyContent: 'flex-end', width: '100%' }}>
          {!record.parentId && (
            <Button
              icon={<PlusOutlined />}
              onClick={() => onCreateChildButtonClick(record)}
            >
              子分类
            </Button>
          )}
          <Button
            icon={<EditOutlined />}
            onClick={() => onUpdateButtonClick(record)}
          >
            编辑
          </Button>
          <Popconfirm
            title="确认删除该分类？"
            okText="删除"
            cancelText="取消"
            onConfirm={() => onRemoveButtonClick(record.id!)}
          >
            <Button icon={<DeleteOutlined />} danger>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const onRequest: NonNullable<
    ProTableProps<
      LibraFortune.Metadata.CategoryDTO,
      GalaxyWeb.EmptyQuery
    >['request']
  > = async (params) => {
    const { pageSize, current } = params;
    const response = await list({
      current: current!,
      pageSize: pageSize!,
      noPage: false,
    });
    const data = response.data.list;
    setTopLevelCategories(data);
    const expandableRowKeys = collectExpandableRowKeys(data);
    setExpandedRowKeys((keys) =>
      keys.filter((key) => expandableRowKeys.has(key)),
    );

    return {
      data,
      success: response.code < 1000,
      total: response.data.total,
    };
  };

  const onFinish = async (
    record: LibraFortune.Metadata.CategoryDTO,
  ): Promise<boolean> => {
    try {
      const payload = {
        ...record,
        icon: record.parentId ? undefined : record.icon,
      };
      const fn = record.id ? update : create;
      await fn(payload);
      actionRef.current?.reload();
      messageApi.open({
        type: 'success',
        content: record.id ? '更新成功' : '新增成功',
      });
      return true;
    } catch {
      return false;
    }
  };

  const onCreateButtonClick = () => {
    form.resetFields();
    setCurrentRecord(undefined);
    setModalVisible(true);
  };

  const onCreateChildButtonClick = (
    record: LibraFortune.Metadata.CategoryDTO,
  ) => {
    const childRecord = {
      parentId: record.id,
    } as LibraFortune.Metadata.CategoryDTO;
    form.resetFields();
    form.setFieldsValue(childRecord);
    setCurrentRecord(childRecord);
    setModalVisible(true);
  };

  const onUpdateButtonClick = (record: LibraFortune.Metadata.CategoryDTO) => {
    form.resetFields();
    form.setFieldsValue(record);
    setCurrentRecord(record);
    setModalVisible(true);
  };

  const onRemoveButtonClick = (id: number) => {
    remove(id).then(() => {
      actionRef.current?.reload();
      messageApi.open({
        type: 'success',
        content: '删除成功',
      });
    });
  };

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<LibraFortune.Metadata.CategoryDTO, GalaxyWeb.EmptyQuery>
        actionRef={actionRef}
        columns={columns}
        defaultSize="small"
        expandable={{
          expandedRowKeys,
          onExpandedRowsChange: (keys) => setExpandedRowKeys([...keys]),
        }}
        rowKey="id"
        request={onRequest}
        search={false}
        scroll={{ x: 920 }}
        toolBarRender={() => [
          <Button
            key="create"
            type="primary"
            icon={<PlusOutlined />}
            onClick={onCreateButtonClick}
          >
            新增一级分类
          </Button>,
        ]}
      />
      <ModalForm<LibraFortune.Metadata.CategoryDTO>
        form={form}
        title={currentRecord?.id ? '编辑分类' : '新增分类'}
        open={modalVisible}
        onOpenChange={(open) => {
          setModalVisible(open);
          if (!open) {
            setCurrentRecord(undefined);
          }
        }}
        onFinish={onFinish}
        modalProps={{
          destroyOnHidden: true,
        }}
      >
        <ProFormText name="id" label="ID" hidden />
        <ProFormSelect
          name="parentId"
          label="父分类"
          options={parentOptions}
          placeholder="不选择则创建一级分类"
          allowClear
          disabled={!!currentRecord?.id || !!currentRecord?.parentId}
        />
        <ProFormText
          name="name"
          label="分类名称"
          placeholder="请输入分类名称"
          rules={[{ required: true }]}
        />
        {!isChildCategory && (
          <ProFormItem name="icon" label="图标">
            <IconPicker />
          </ProFormItem>
        )}
      </ModalForm>
    </PageContainer>
  );
};

export default MetadataCategory;
