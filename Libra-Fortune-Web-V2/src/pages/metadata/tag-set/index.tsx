import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormSwitch,
  ProFormText,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { Button, Form, message, Popconfirm, Space, Tag } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import {
  create,
  createItem,
  list,
  remove,
  removeItem,
  update,
  updateItem,
} from '@/services/libra-fortune/metadata/tag-set';

type TagItemRecord = LibraFortune.Metadata.TagSetItemDTO & {
  recordType: 'item';
};

type TagSetRecord = LibraFortune.Metadata.TagSetDTO & {
  children?: TagItemRecord[];
  recordType: 'set';
};

type TagTableRecord = TagSetRecord | TagItemRecord;

type TagFormValues = Partial<
  LibraFortune.Metadata.TagSetDTO & LibraFortune.Metadata.TagSetItemDTO
>;

const appendChildren = (
  records: LibraFortune.Metadata.TagSetDTO[],
): TagTableRecord[] =>
  records.map((record) => ({
    ...record,
    recordType: 'set',
    children: record.items?.map((item) => ({
      ...item,
      setId: record.id,
      recordType: 'item',
    })),
  }));

const collectExpandableRowKeys = (records: TagTableRecord[]): Set<React.Key> =>
  records.reduce<Set<React.Key>>((keys, record) => {
    if (record.recordType === 'set' && record.id && record.children?.length) {
      keys.add(`${record.recordType}-${record.id}`);
    }

    return keys;
  }, new Set<React.Key>());

const MetadataTagSet: React.FC = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const [modalVisible, setModalVisible] = useState(false);
  const [modalType, setModalType] = useState<'set' | 'item'>('set');
  const [currentRecord, setCurrentRecord] = useState<TagTableRecord | undefined>(
    undefined,
  );
  const [expandedRowKeys, setExpandedRowKeys] = useState<React.Key[]>([]);
  const [form] = Form.useForm<TagFormValues>();
  const actionRef = useRef<ActionType | null>(null);

  useEffect(() => {
    if (!modalVisible) return;

    if (currentRecord) {
      form.setFieldsValue(currentRecord);
    } else {
      form.resetFields();
    }
  }, [modalVisible, currentRecord, form]);

  const columns: ProColumns<TagTableRecord>[] = [
    {
      dataIndex: 'id',
      title: 'ID',
      search: false,
      width: 104,
      renderText: (value: number) => <Tag>{value}</Tag>,
    },
    {
      dataIndex: 'name',
      title: '标签名称',
      copyable: true,
      search: false,
    },
    {
      dataIndex: 'required',
      title: '必选',
      search: false,
      width: 100,
      render: (_, record) =>
        record.recordType === 'set' ? (
          <Tag color={record.required ? 'blue' : undefined}>
            {record.required ? '必选' : '可选'}
          </Tag>
        ) : (
          <span>-</span>
        ),
    },
    {
      dataIndex: 'allowMultiple',
      title: '多选',
      search: false,
      width: 100,
      render: (_, record) =>
        record.recordType === 'set' ? (
          <Tag color={record.allowMultiple ? 'blue' : undefined}>
            {record.allowMultiple ? '多选' : '单选'}
          </Tag>
        ) : (
          <span>-</span>
        ),
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
          {record.recordType === 'set' && (
            <Button
              icon={<PlusOutlined />}
              onClick={() => onCreateItemButtonClick(record)}
            >
              子标签
            </Button>
          )}
          <Button
            icon={<EditOutlined />}
            onClick={() => onUpdateButtonClick(record)}
          >
            编辑
          </Button>
          <Popconfirm
            title={`确认删除该${record.recordType === 'set' ? '标签组' : '标签'}？`}
            okText="删除"
            cancelText="取消"
            onConfirm={() => onRemoveButtonClick(record)}
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
    ProTableProps<TagTableRecord, GalaxyWeb.EmptyQuery>['request']
  > = async (params) => {
    const { pageSize, current } = params;
    const response = await list({
      current: current!,
      pageSize: pageSize!,
      noPage: false,
    });
    const data = appendChildren(response.data.list);
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

  const onFinish = async (record: TagFormValues): Promise<boolean> => {
    try {
      if (modalType === 'set') {
        const data = {
          id: record.id,
          name: record.name!,
          required: record.required ?? false,
          allowMultiple: record.allowMultiple ?? false,
        } as LibraFortune.Metadata.TagSetDTO;
        const fn = data.id ? update : create;
        await fn(data);
      } else {
        const setId = record.setId!;
        const data = {
          id: record.id,
          setId,
          name: record.name!,
        } as LibraFortune.Metadata.TagSetItemDTO;
        const fn = data.id ? updateItem : createItem;
        await fn(setId, data);
      }

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
    setModalType('set');
    setCurrentRecord(undefined);
    setModalVisible(true);
  };

  const onCreateItemButtonClick = (record: TagTableRecord) => {
    setModalType('item');
    setCurrentRecord({
      recordType: 'item',
      setId: record.id,
    } as TagItemRecord);
    setModalVisible(true);
  };

  const onUpdateButtonClick = (record: TagTableRecord) => {
    setModalType(record.recordType);
    setCurrentRecord(record);
    setModalVisible(true);
  };

  const onRemoveButtonClick = (record: TagTableRecord) => {
    const request =
      record.recordType === 'set'
        ? remove(record.id!)
        : removeItem(record.setId!, record.id!);
    request.then(() => {
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
      <ProTable<TagTableRecord, GalaxyWeb.EmptyQuery>
        actionRef={actionRef}
        columns={columns}
        defaultSize="small"
        expandable={{
          expandedRowKeys,
          onExpandedRowsChange: (keys) => setExpandedRowKeys([...keys]),
        }}
        rowKey={(record) => `${record.recordType}-${record.id}`}
        request={onRequest}
        search={false}
        scroll={{ x: 860 }}
        toolBarRender={() => [
          <Button
            key="create"
            type="primary"
            icon={<PlusOutlined />}
            onClick={onCreateButtonClick}
          >
            新增标签组
          </Button>,
        ]}
      />
      <ModalForm<TagFormValues>
        form={form}
        title={
          currentRecord?.id
            ? `编辑${modalType === 'set' ? '标签组' : '标签'}`
            : `新增${modalType === 'set' ? '标签组' : '标签'}`
        }
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
        <ProFormText name="setId" label="标签组ID" hidden />
        <ProFormText
          name="name"
          label={modalType === 'set' ? '标签组名称' : '标签名称'}
          placeholder={`请输入${modalType === 'set' ? '标签组名称' : '标签名称'}`}
          rules={[{ required: true }]}
        />
        {modalType === 'set' && (
          <>
            <ProFormSwitch
              name="required"
              label="必选"
              fieldProps={{
                checkedChildren: '是',
                unCheckedChildren: '否',
              }}
            />
            <ProFormSwitch
              name="allowMultiple"
              label="多选"
              fieldProps={{
                checkedChildren: '是',
                unCheckedChildren: '否',
              }}
            />
          </>
        )}
      </ModalForm>
    </PageContainer>
  );
};

export default MetadataTagSet;
