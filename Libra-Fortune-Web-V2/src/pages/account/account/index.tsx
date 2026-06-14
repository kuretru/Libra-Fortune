import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormItem,
  ProFormSwitch,
  ProFormText,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { Button, Form, message, Popconfirm, Space, Tag } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import IconPicker, { getAntIcon } from '@/components/IconPicker';
import {
  create,
  list,
  remove,
  update,
} from '@/services/libra-fortune/account/account';

type AccountSearchParams = {
  name?: string;
  canHoldFunds?: boolean | 'true' | 'false';
};

const Account: React.FC = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const [modalVisible, setModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<
    LibraFortune.Account.AccountDTO | undefined
  >(undefined);
  const [form] = Form.useForm<LibraFortune.Account.AccountDTO>();
  const actionRef = useRef<ActionType | null>(null);

  useEffect(() => {
    if (!modalVisible) return;

    if (currentRecord) {
      form.setFieldsValue(currentRecord);
    } else {
      form.resetFields();
      form.setFieldsValue({ canHoldFunds: true });
    }
  }, [modalVisible, currentRecord, form]);

  const columns: ProColumns<LibraFortune.Account.AccountDTO>[] = [
    {
      dataIndex: 'id',
      title: 'ID',
      valueType: 'indexBorder',
      width: 72,
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
      dataIndex: 'name',
      title: '账户名称',
      copyable: true,
    },
    {
      dataIndex: 'canHoldFunds',
      title: '可持有资金',
      valueType: 'select',
      valueEnum: {
        true: { text: '是' },
        false: { text: '否' },
      },
      render: (_, record) =>
        record.canHoldFunds ? (
          <Tag color="success">是</Tag>
        ) : (
          <Tag color="default">否</Tag>
        ),
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
            title="确认删除该账户？"
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
      LibraFortune.Account.AccountDTO,
      AccountSearchParams
    >['request']
  > = async (params) => {
    const { pageSize, current, name, canHoldFunds } = params;
    const canHoldFundsQuery =
      typeof canHoldFunds === 'string'
        ? canHoldFunds === 'true'
        : canHoldFunds;
    const response = await list({
      current: current!,
      pageSize: pageSize!,
      noPage: false,
      nameLike: name,
      canHoldFunds: canHoldFundsQuery,
    });

    return {
      data: response.data.list,
      success: response.code < 1000,
      total: response.data.total,
    };
  };

  const onFinish = async (
    record: LibraFortune.Account.AccountDTO,
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

  const onUpdateButtonClick = (record: LibraFortune.Account.AccountDTO) => {
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
      <ProTable<LibraFortune.Account.AccountDTO, AccountSearchParams>
        actionRef={actionRef}
        columns={columns}
        defaultSize="small"
        rowKey="id"
        request={onRequest}
        search={{
          labelWidth: 'auto',
        }}
        scroll={{ x: 760 }}
        toolBarRender={() => [
          <Button
            key="create"
            type="primary"
            icon={<PlusOutlined />}
            onClick={onCreateButtonClick}
          >
            新增账户
          </Button>,
        ]}
      />
      <ModalForm<LibraFortune.Account.AccountDTO>
        form={form}
        title={currentRecord?.id ? '编辑账户' : '新增账户'}
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
          name="name"
          label="账户名称"
          placeholder="请输入账户名称"
          rules={[{ required: true }]}
        />
        <ProFormSwitch
          name="canHoldFunds"
          label="可持有资金"
          fieldProps={{
            checkedChildren: '是',
            unCheckedChildren: '否',
          }}
        />
        <ProFormItem name="icon" label="图标">
          <IconPicker />
        </ProFormItem>
      </ModalForm>
    </PageContainer>
  );
};

export default Account;
