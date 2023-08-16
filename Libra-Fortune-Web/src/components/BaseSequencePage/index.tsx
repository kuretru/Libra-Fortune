import type BaseSequenceService from '@/services/galaxy-web/base-sequence-service';
import {
  DeleteOutlined,
  DragOutlined,
  EditOutlined,
  PlusOutlined,
  QuestionCircleOutlined,
  SortAscendingOutlined,
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
import { arrayMoveImmutable } from 'array-move';
import { isEqual } from 'lodash';
import React from 'react';
import { SortableContainer, SortableElement, SortableHandle } from 'react-sortable-hoc';
import './drag.less';

const { confirm } = Modal;
const DragHandle = SortableHandle(() => <DragOutlined style={{ color: '#999', cursor: 'grab' }} />);
const SortableItem = SortableElement((props: any) => <tr {...props} />);
const SortContainer = SortableContainer((props: any) => <tbody {...props} />);

/**
 * T -> DTO
 * Q -> Query
 * V -> VO default same as DTO
 */
interface IBaseSequencePageProps<
  T extends API.BaseDTO,
  Q extends API.PaginationQuery,
  V extends API.BaseDTO = T,
> {
  pageName: string;
  service: BaseSequenceService<T, Q, V>;
  columns: ProColumns<T | V>[];
  formItem: JSX.Element;
  transformFormValues?: (record: T) => T;
  tableValueToFormValue?: (record: V) => T;
  onFormValuesChange?: (
    changedValues: any,
    values: T,
    formRef: React.MutableRefObject<FormInstance>,
  ) => void;
  onSubmit?: (params: Q) => Q;
  tableProps?: ProTableProps<T | V, Q>;
  modalProps?: ModalFormProps<T>;
}

interface IBaseSequencePageState<V> {
  modalVisible: boolean;
  tableLoading: boolean;
  useLocalData: boolean;
  dataSource: API.ProTableData<V>;
}

abstract class BaseSequencePage<
  T extends API.BaseDTO,
  Q extends API.PaginationQuery,
  V extends API.BaseDTO = T,
> extends React.Component<IBaseSequencePageProps<T, Q, V>, IBaseSequencePageState<V>> {
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
              onClick={() => this.onEditButtonClick(record as V)}
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
    {
      align: 'center',
      dataIndex: 'sort',
      search: false,
      title: '排序',
      width: 60,
      render: () => <DragHandle />,
    },
  ];
  formRef: React.MutableRefObject<ProFormInstance>;
  tableRef: React.RefObject<ActionType>;
  defaultFormValue: Record<string, any>;

  constructor(props: IBaseSequencePageProps<T, Q, V>) {
    super(props);
    this.state = {
      modalVisible: false,
      tableLoading: false,
      useLocalData: false,
      dataSource: { success: false, data: [], current: 0, pageSize: 0, total: 0 },
    };
    this.formRef = React.createRef<ProFormInstance>() as React.MutableRefObject<ProFormInstance>;
    this.tableRef = React.createRef<ActionType>();
    this.defaultFormValue = {};
  }

  componentDidUpdate(prevProps: IBaseSequencePageProps<T, Q, V>) {
    if (!isEqual(this.props.service, prevProps.service)) {
      this.tableRef.current?.reloadAndRest?.();
    }
  }

  fetchData = async (params: Q & API.PaginationQuery) => {
    if (this.state.useLocalData) {
      this.setState({ useLocalData: false });
      return this.state.dataSource;
    }
    const response = await this.props.service.listByPage(params);
    this.setState({ dataSource: response });
    return response;
  };

  onAddButtonClick = () => {
    this.formRef.current?.resetFields();
    this.formRef.current.setFieldsValue(this.defaultFormValue);
    this.setState({ modalVisible: true });
  };

  onEditButtonClick = (record: V) => {
    const recordDto = this.props.tableValueToFormValue?.(record) ?? record;
    this.formRef.current.setFieldsValue(recordDto);
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

  onReorderButtonClick = () => {
    const idList: string[] = [];
    this.state.dataSource.data.forEach((record) => {
      idList.push(record.id!);
    });
    this.props.service.reorder(idList).then((response) => {
      message.success(response.data);
    });
    this.tableRef.current?.reload();
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
          components={{ body: { row: this.DraggableBodyRow, wrapper: this.DraggableContainer } }}
          dataSource={this.state.dataSource.data}
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
            <Button
              key="reorder"
              icon={<SortAscendingOutlined />}
              onClick={this.onReorderButtonClick}
            >
              重新排序
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

  onSortEnd = ({ oldIndex, newIndex }: { oldIndex: number; newIndex: number }) => {
    if (oldIndex !== newIndex) {
      const newData = arrayMoveImmutable(
        [...this.state.dataSource.data],
        oldIndex,
        newIndex,
      ).filter((el: any) => !!el);
      const dataSource = this.state.dataSource;
      dataSource.data = newData;
      this.setState({ useLocalData: true, dataSource: dataSource });
    }
  };

  DraggableContainer = (props: any) => (
    <SortContainer
      useDragHandle
      disableAutoscroll
      helperClass="row-dragging"
      onSortEnd={this.onSortEnd}
      {...props}
    />
  );

  DraggableBodyRow = (props: any) => {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { className, style, ...restProps } = props;
    const index = this.state.dataSource.data.findIndex((x) => x.id === restProps['data-row-key']);
    return <SortableItem index={index} {...restProps} />;
  };
}

export default BaseSequencePage;
