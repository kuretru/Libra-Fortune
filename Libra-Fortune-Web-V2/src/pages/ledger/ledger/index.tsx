import {
  DeleteOutlined,
  EditOutlined,
  FormOutlined,
  PlusOutlined,
} from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormText,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { history, useModel } from '@umijs/max';
import {
  Button,
  Form,
  Input,
  InputNumber,
  message,
  Popconfirm,
  Space,
  Table,
  Tag,
} from 'antd';
import React, { useEffect, useMemo, useRef, useState } from 'react';
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

const formatPercent = (value?: string): string => {
  if (!value) return '-';
  return `${value}%`;
};

const Ledger: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const [messageApi, contextHolder] = message.useMessage();
  const [modalVisible, setModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<
    LibraFortune.Ledger.LedgerDTO | undefined
  >(undefined);
  const [form] = Form.useForm<LibraFortune.Ledger.LedgerDTO>();
  const actionRef = useRef<ActionType | null>(null);

  const currentUsername =
    initialState?.currentUser?.userid ?? initialState?.currentUser?.name;

  const defaultMembers = useMemo(
    () =>
      currentUsername
        ? [
            {
              username: currentUsername,
              defaultFundedRatio: '100.00',
            },
          ]
        : [],
    [currentUsername],
  );

  const normalizeMembers = (
    members?: LibraFortune.Ledger.LedgerMemberDTO[],
  ): LibraFortune.Ledger.LedgerMemberDTO[] => {
    const result = members ?? [];
    if (
      currentUsername &&
      !result.some((member) => member.username === currentUsername)
    ) {
      return [...defaultMembers, ...result];
    }
    return result;
  };

  useEffect(() => {
    if (!modalVisible) return;

    if (currentRecord) {
      form.setFieldsValue({
        ...currentRecord,
        members: normalizeMembers(currentRecord.members),
      });
    } else {
      form.resetFields();
      form.setFieldValue('members', defaultMembers);
    }
  }, [modalVisible, currentRecord, defaultMembers, form]);

  const columns: ProColumns<LibraFortune.Ledger.LedgerDTO>[] = [
    {
      dataIndex: 'id',
      title: 'ID',
      search: false,
      width: 64,
      renderText: (value: number) => <Tag>{value}</Tag>,
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
                {` ${formatPercent(member.defaultFundedRatio)}`}
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
      width: 300,
      render: (_, record) => (
        <Space>
          <Button
            icon={<FormOutlined />}
            onClick={() => onStartEntryButtonClick(record)}
          >
            开始记账
          </Button>
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
        members: normalizeMembers(record.members),
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

  const onStartEntryButtonClick = (record: LibraFortune.Ledger.LedgerDTO) => {
    if (!record.id) return;
    history.push(`/ledger/${record.id}/entry`);
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
        }}
      >
        <ProFormText name="id" label="ID" hidden />
        <ProFormText
          name="name"
          label="账本名称"
          placeholder="请输入账本名称"
          rules={[{ required: true }]}
        />
        <Form.Item label="成员" required>
          <Form.List name="members">
            {(fields, { add, remove }) => (
              <Space direction="vertical" style={{ width: '100%' }}>
                <Table
                  columns={[
                    {
                      dataIndex: 'username',
                      title: '用户名',
                      render: (_, field) => (
                        <>
                          <Form.Item name={[field.name, 'id']} hidden />
                          <Form.Item
                            name={[field.name, 'username']}
                            rules={[{ required: true }]}
                            style={{ marginBottom: 0 }}
                          >
                            <Input
                              disabled={
                                form.getFieldValue([
                                  'members',
                                  field.name,
                                  'username',
                                ]) === currentUsername
                              }
                              placeholder="请输入用户名"
                            />
                          </Form.Item>
                        </>
                      ),
                    },
                    {
                      dataIndex: 'defaultFundedRatio',
                      title: '默认承担比例',
                      width: 180,
                      render: (_, field) => (
                        <Form.Item
                          name={[field.name, 'defaultFundedRatio']}
                          rules={[{ required: true }]}
                          style={{ marginBottom: 0 }}
                        >
                          <InputNumber
                            addonAfter="%"
                            max={100}
                            min={0}
                            precision={2}
                            step="0.01"
                            stringMode
                            style={{ width: '100%' }}
                          />
                        </Form.Item>
                      ),
                    },
                    {
                      key: 'action',
                      title: '操作',
                      width: 88,
                      render: (_, field) => {
                        const username = form.getFieldValue([
                          'members',
                          field.name,
                          'username',
                        ]);
                        return (
                          <Button
                            danger
                            disabled={username === currentUsername}
                            icon={<DeleteOutlined />}
                            onClick={() => remove(field.name)}
                          />
                        );
                      },
                    },
                  ]}
                  dataSource={fields}
                  pagination={false}
                  rowKey="key"
                  size="small"
                />
                <Button
                  icon={<PlusOutlined />}
                  onClick={() =>
                    add({ defaultFundedRatio: '0.00', username: '' })
                  }
                >
                  新增成员
                </Button>
              </Space>
            )}
          </Form.List>
        </Form.Item>
      </ModalForm>
    </PageContainer>
  );
};

export default Ledger;
