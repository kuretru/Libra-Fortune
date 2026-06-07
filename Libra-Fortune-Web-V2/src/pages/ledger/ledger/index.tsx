import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormDigit,
  ProFormList,
  ProFormText,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { Button, Form, message, Popconfirm, Space, Tag } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import {
  create,
  get,
  list,
  remove,
  update,
} from '@/services/libra-fortune/ledger/ledger';

type LedgerSearchParams = LibraFortune.Ledger.LedgerQuery & {
  name?: string;
};

const Ledger: React.FC = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const [modalVisible, setModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<
    LibraFortune.Ledger.LedgerDTO | undefined
  >(undefined);
  const [form] = Form.useForm<LibraFortune.Ledger.LedgerDTO>();
  const actionRef = useRef<ActionType | null>(null);

  useEffect(() => {
    if (!modalVisible) return;

    if (currentRecord) {
      form.setFieldsValue({
        ...currentRecord,
        members: currentRecord.members ?? [],
      });
    } else {
      form.resetFields();
      form.setFieldValue('members', []);
    }
  }, [modalVisible, currentRecord, form]);

  const columns: ProColumns<LibraFortune.Ledger.LedgerDTO>[] = [
    {
      dataIndex: 'id',
      title: 'ID',
      valueType: 'indexBorder',
      width: 72,
    },
    {
      dataIndex: 'name',
      title: '账本名称',
      copyable: true,
    },
    {
      dataIndex: 'owner',
      title: 'Owner',
      copyable: true,
      search: false,
      width: 180,
    },
    {
      dataIndex: 'members',
      title: '成员',
      search: false,
      width: 280,
      render: (_, record) => {
        const members = record.members ?? [];
        if (members.length === 0) {
          return <span>-</span>;
        }
        return (
          <Space size={[0, 4]} wrap>
            {members.map((member) => (
              <Tag key={`${record.id}-${member.username}`}>
                {member.username}
                {` ${member.defaultFundedRatio}`}
              </Tag>
            ))}
          </Space>
        );
      },
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
            title="确认删除该账本？"
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
    ProTableProps<LibraFortune.Ledger.LedgerDTO, LedgerSearchParams>['request']
  > = async (params) => {
    const { pageSize, current, name } = params;
    const response = await list({
      current: current!,
      pageSize: pageSize!,
      noPage: false,
      nameLike: name,
    });

    return {
      data: response.data.list,
      success: response.code < 1000,
      total: response.data.total,
    };
  };

  const onFinish = async (
    record: LibraFortune.Ledger.LedgerDTO,
  ): Promise<boolean> => {
    try {
      const fn = record.id ? update : create;
      await fn({
        ...record,
        members: record.members ?? [],
      });
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

  const onUpdateButtonClick = async (record: LibraFortune.Ledger.LedgerDTO) => {
    if (!record.id) return;
    const response = await get(record.id);
    setCurrentRecord(response.data);
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
      <ProTable<LibraFortune.Ledger.LedgerDTO, LedgerSearchParams>
        actionRef={actionRef}
        columns={columns}
        defaultSize="small"
        rowKey="id"
        request={onRequest}
        scroll={{ x: 800 }}
        toolBarRender={() => [
          <Button
            key="create"
            type="primary"
            icon={<PlusOutlined />}
            onClick={onCreateButtonClick}
          >
            新增账本
          </Button>,
        ]}
      />
      <ModalForm<LibraFortune.Ledger.LedgerDTO>
        form={form}
        title={currentRecord?.id ? '编辑账本' : '新增账本'}
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
          name="name"
          label="账本名称"
          placeholder="请输入账本名称"
          rules={[{ required: true }]}
        />
        <ProFormList
          name="members"
          label="成员"
          creatorButtonProps={{
            creatorButtonText: '新增成员',
          }}
          copyIconProps={false}
          itemRender={({ listDom, action }) => (
            <Space align="baseline">
              {listDom}
              {action}
            </Space>
          )}
        >
          <ProFormText
            name="username"
            label="用户名"
            placeholder="请输入用户名"
            rules={[{ required: true }]}
          />
          <ProFormDigit
            name="defaultFundedRatio"
            label="默认承担比例"
            placeholder="请输入比例"
            min={0}
            max={1}
            fieldProps={{
              precision: 2,
              stringMode: true,
              step: '0.01',
            }}
            rules={[{ required: true }]}
          />
        </ProFormList>
      </ModalForm>
    </PageContainer>
  );
};

export default Ledger;
