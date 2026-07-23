import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormText,
  type ProTableProps,
} from '@ant-design/pro-components';
import { DragSortTable } from '@ant-design/pro-components/es/table';
import { Button, Form, message, Popconfirm, Space, Tag } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import {
  create,
  list,
  remove,
  reorder,
  update,
} from '@/services/libra-fortune/metadata/currency';

const MetadataCurrency: React.FC = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const [modalVisible, setModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<
    LibraFortune.Metadata.CurrencyDTO | undefined
  >(undefined);
  const [form] = Form.useForm<LibraFortune.Metadata.CurrencyDTO>();
  const actionRef = useRef<ActionType | null>(null);

  useEffect(() => {
    if (!modalVisible) return;

    if (currentRecord) {
      form.setFieldsValue(currentRecord);
    } else {
      form.resetFields();
    }
  }, [modalVisible, currentRecord, form]);

  const columns: ProColumns<LibraFortune.Metadata.CurrencyDTO>[] = [
    {
      dataIndex: 'sort',
      title: '排序',
      search: false,
      width: 64,
    },
    {
      dataIndex: 'id',
      title: 'ID',
      search: false,
      width: 64,
      renderText: (value: number) => <Tag>{value}</Tag>,
    },
    {
      dataIndex: 'code',
      title: '货币代码',
      copyable: true,
      search: false,
      width: 160,
    },
    {
      dataIndex: 'symbol',
      title: '货币符号',
      search: false,
      width: 120,
    },
    {
      dataIndex: 'name',
      title: '货币名称',
      search: false,
    },
    {
      key: 'action',
      title: '操作',
      fixed: 'right',
      valueType: 'option',
      width: 180,
      render: (_, record) => (
        <Space>
          <Button
            icon={<EditOutlined />}
            onClick={() => onUpdateButtonClick(record)}
          >
            编辑
          </Button>
          <Popconfirm
            title="确认删除该货币？"
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
      LibraFortune.Metadata.CurrencyDTO,
      GalaxyWeb.EmptyQuery
    >['request']
  > = async (params) => {
    const { pageSize, current } = params;
    const response = await list({
      current: current ?? 1,
      pageSize: pageSize ?? 1000,
      noPage: true,
    });

    return {
      data: response.data.list,
      success: response.code < 1000,
      total: response.data.total,
    };
  };

  const onFinish = async (
    record: LibraFortune.Metadata.CurrencyDTO,
  ): Promise<boolean> => {
    try {
      const fn = record.id ? update : create;
      await fn(record);
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
    setCurrentRecord(undefined);
    setModalVisible(true);
  };

  const onUpdateButtonClick = (record: LibraFortune.Metadata.CurrencyDTO) => {
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

  const onDragSortEnd = async (
    _beforeIndex: number,
    _afterIndex: number,
    newDataSource: LibraFortune.Metadata.CurrencyDTO[],
  ) => {
    await reorder(newDataSource.map((record) => record.id!));
    actionRef.current?.reload();
    messageApi.open({
      type: 'success',
      content: '排序已保存',
    });
  };

  return (
    <PageContainer>
      {contextHolder}
      <DragSortTable<LibraFortune.Metadata.CurrencyDTO, GalaxyWeb.EmptyQuery>
        actionRef={actionRef}
        columns={columns}
        dragSortKey="sort"
        onDragSortEnd={onDragSortEnd}
        pagination={false}
        rowKey="id"
        request={onRequest}
        search={false}
        scroll={{ x: 704 }}
        toolBarRender={() => [
          <Button
            key="create"
            type="primary"
            icon={<PlusOutlined />}
            onClick={onCreateButtonClick}
          >
            新增
          </Button>,
        ]}
      />
      <ModalForm<LibraFortune.Metadata.CurrencyDTO>
        form={form}
        title={currentRecord?.id ? '编辑货币' : '新增货币'}
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
        <ProFormText
          name="code"
          label="货币代码"
          placeholder="请输入货币代码"
          rules={[{ required: true }]}
        />
        <ProFormText
          name="symbol"
          label="货币符号"
          placeholder="请输入货币符号"
          rules={[{ required: true }]}
        />
        <ProFormText
          name="name"
          label="货币名称"
          placeholder="请输入货币名称"
          rules={[{ required: true }]}
        />
      </ModalForm>
    </PageContainer>
  );
};

export default MetadataCurrency;
