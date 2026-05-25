import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormText,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { Button, Form, message, Space } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import {
  create,
  list,
  remove,
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
      dataIndex: 'id',
      title: 'ID',
      colSize: 1,
      valueType: 'indexBorder',
    },
    {
      dataIndex: 'code',
      title: '货币代码',
      colSize: 4,
      copyable: true,
      search: false,
    },
    {
      dataIndex: 'symbol',
      title: '货币符号',
      colSize: 2,
      search: false,
    },
    {
      dataIndex: 'name',
      title: '货币名称',
      colSize: 2,
      search: false,
    },
    {
      key: 'action',
      title: '操作',
      valueType: 'option',
      render: (_, record) => (
        <Space>
          <Button onClick={() => onUpdateButtonClick(record)}>编辑</Button>
          <Button onClick={() => onRemoveButtonClick(record.id!)} danger>
            删除
          </Button>
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
      current: current!,
      pageSize: pageSize!,
      noPage: false,
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

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<LibraFortune.Metadata.CurrencyDTO, GalaxyWeb.EmptyQuery>
        actionRef={actionRef}
        columns={columns}
        rowKey="id"
        request={onRequest}
        toolBarRender={() => [
          <Button key="create" type="primary" onClick={onCreateButtonClick}>
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
          maskClosable: false,
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
