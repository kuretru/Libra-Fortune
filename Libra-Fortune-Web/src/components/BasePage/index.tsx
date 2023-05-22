import type BaseService from '@/services/galaxy-web/base-service';
import {
  DeleteOutlined,
  EditOutlined,
  PlusOutlined,
  QuestionCircleOutlined,
} from '@ant-design/icons';
import type {
  ActionType,
  ModalFormProps,
  ProColumns,
  ProFormInstance,
  ProTableProps,
} from '@ant-design/pro-components';
import { ModalForm, ProFormText, ProTable } from '@ant-design/pro-components';
import type { FormInstance } from 'antd';
import { Button, message, Modal, Space } from 'antd';
import { isEqual } from 'lodash';
import React from 'react';

const { confirm } = Modal;

/**
 * T -> DTO
 * Q -> Query
 * V -> VO default same as DTO
 */
interface IBasePageProps<
  T extends API.BaseDTO,
  Q extends API.PaginationQuery,
  V extends API.BaseDTO = T,
> {
  pageName: string;
  service: BaseService<T, Q, V>;
  columns: ProColumns<T | V>[];
  formItem: JSX.Element;
  transformFormValues?: (record: T) => T;
  onFormValuesChange?: (
    changedValues: any,
    values: T,
    formRef: React.MutableRefObject<FormInstance>,
  ) => void;
  onSubmit?: (params: Q) => Q;
  tableProps?: ProTableProps<T | V, Q>;
  modalProps?: ModalFormProps<T>;
}

interface IBasePageState {
  modalVisible: boolean;
  tableLoading: boolean;
}

abstract class BasePage<
  T extends API.BaseDTO,
  Q extends API.PaginationQuery,
  V extends API.BaseDTO = T,
> extends React.Component<IBasePageProps<T, Q, V>, IBasePageState> {
  columnsPrefix: ProColumns<T | V>[] = [
    {
      align: 'center',
      key: 'index',
      title: '序号',
      valueType: 'indexBorder',
      width: 60,
    },
  ];
  columnsSuffix: ProColumns<T | V>[] = [
    {
      align: 'center',
      key: 'action',
      search: false,
      title: '操作',
      width: 240,
      render: (_, record) => {
        return (
          <Space>
            <Button
              icon={<EditOutlined />}
              key="edit"
              onClick={() => this.onEditButtonClick(record as T)}
              type="primary"
            >
              编辑
            </Button>
            <Button
              danger
              icon={<DeleteOutlined />}
              key="delete"
              onClick={() => this.onDeleteButtonClick(record.id!)}
              type="primary"
            >
              删除
            </Button>
          </Space>
        );
      },
    },
  ];
  formRef: React.MutableRefObject<ProFormInstance>;
  tableRef: React.RefObject<ActionType>;
  defaultFormValue: Record<string, any>;

  constructor(props: IBasePageProps<T, Q, V>) {
    super(props);
    this.state = {
      modalVisible: false,
      tableLoading: false,
    };
    this.formRef = React.createRef<ProFormInstance>() as React.MutableRefObject<ProFormInstance>;
    this.tableRef = React.createRef<ActionType>();
    this.defaultFormValue = {};
  }

  componentDidUpdate(prevProps: IBasePageProps<T, Q, V>) {
    if (!isEqual(this.props.service, prevProps.service)) {
      this.tableRef.current?.reloadAndRest?.();
    }
  }

  fetchData = async (params: Q & API.PaginationQuery) => {
    return this.props.service.listByPage(params);
  };

  onAddButtonClick = () => {
    this.formRef.current?.resetFields();
    this.setState({ modalVisible: true });
  };

  onEditButtonClick = (record: T) => {
    this.formRef.current.setFieldsValue(record);
    this.setState({ modalVisible: true });
  };

  onDeleteButtonClick = (id: string) => {
    const messageKey = 'delete';
    const service = this.props.service;
    const tableRef = this.tableRef;
    confirm({
      title: `确定删除这个${this.props.pageName}吗？`,
      icon: <QuestionCircleOutlined />,
      okType: 'danger',
      onOk() {
        message.loading({ content: '请求处理中...', duration: 0, key: messageKey });
        service
          .remove(id)
          .then(() => {
            tableRef.current?.reload();
            message.success({ content: '删除成功！', key: messageKey });
          })
          .catch(() => {
            message.destroy(messageKey);
          });
      },
    });
  };

  onFormFinish = async (formData: T) => {
    const record = this.props.transformFormValues?.(formData) ?? formData;
    const messageKey = 'create';
    let result = false;
    message.loading({ content: '请求处理中...', duration: 0, key: messageKey });
    if (record.id) {
      await this.props.service
        .update(record)
        .then(() => {
          this.tableRef.current?.reload();
          message.success({ content: '修改成功！', key: messageKey });
          result = true;
        })
        .catch(() => {
          message.destroy(messageKey);
        });
    } else {
      await this.props.service
        .create(record)
        .then(() => {
          this.tableRef.current?.reload();
          message.success({ content: '新增成功！', key: messageKey });
          result = true;
        })
        .catch(() => {
          message.destroy(messageKey);
        });
    }
    return result;
  };

  onFormValuesChange = (changedValues: any, values: T) => {
    this.props.onFormValuesChange?.(changedValues, values, this.formRef);
  };

  onSubmit = (params: Q) => {
    if (this.props.onSubmit) {
      this.defaultFormValue = this.props.onSubmit(params);
    }
  };

  onReset = () => {
    this.defaultFormValue = {};
  };

  render() {
    return (
      <>
        <ProTable<T | V, Q>
          actionRef={this.tableRef}
          bordered
          columns={this.columnsPrefix.concat(this.props.columns).concat(this.columnsSuffix)}
          headerTitle={`${this.props.pageName}管理`}
          loading={this.state.tableLoading}
          onSubmit={this.onSubmit}
          onReset={this.onReset}
          options={{ fullScreen: true, setting: true }}
          pagination={{ defaultPageSize: 20, showSizeChanger: true }}
          request={this.fetchData}
          rowKey="id"
          tooltip={`${this.props.pageName}管理`}
          toolBarRender={() => [
            <Button
              icon={<PlusOutlined />}
              key="add"
              onClick={this.onAddButtonClick}
              type="primary"
            >
              新增{this.props.pageName}
            </Button>,
          ]}
          {...this.props.tableProps}
        />
        <ModalForm<T>
          formRef={this.formRef}
          modalProps={{ forceRender: true }}
          onFinish={this.onFormFinish}
          onValuesChange={this.onFormValuesChange}
          onOpenChange={(visible) => this.setState({ modalVisible: visible })}
          title={
            this.formRef.current?.getFieldValue('id')
              ? `编辑${this.props.pageName}`
              : `新增${this.props.pageName}`
          }
          open={this.state.modalVisible}
          submitter={{
            render: (props, defaultDoms) => {
              return [
                defaultDoms[0],
                <Button danger key="reset" onClick={() => props.reset()}>
                  重置
                </Button>,
                defaultDoms[1],
              ];
            },
            searchConfig: { resetText: '取消', submitText: '提交' },
          }}
          {...this.props.modalProps}
        >
          <ProFormText disabled hidden label="ID" name="id" width="lg" />
          {this.props.formItem}
        </ModalForm>
      </>
    );
  }
}

export default BasePage;
