import * as AntIcons from '@ant-design/icons';
import { Button, Flex, Input, Space, Tag, Tooltip } from 'antd';
import React, { useMemo, useState } from 'react';

const iconNames = Object.keys(AntIcons).filter((name) => /Outlined$/.test(name));

const getIconLabel = (icon: string) => icon.replace(/Outlined$/, '');

export const getAntIcon = (icon?: string): React.ElementType | undefined => {
  if (!icon) {
    return undefined;
  }

  // biome-ignore lint/performance/noDynamicNamespaceImportAccess: icon names are persisted as strings by the backend.
  const Icon = AntIcons[icon as keyof typeof AntIcons];
  if (!Icon) {
    return undefined;
  }
  return Icon as React.ElementType;
};

type IconPickerProps = {
  value?: string;
  onChange?: (value?: string) => void;
};

const IconPicker: React.FC<IconPickerProps> = ({ value, onChange }) => {
  const [keyword, setKeyword] = useState('');
  const selectedIcon = getAntIcon(value);
  const filteredIconNames = useMemo(() => {
    const normalizedKeyword = keyword.trim().toLowerCase();
    if (!normalizedKeyword) {
      return iconNames;
    }
    return iconNames.filter((icon) =>
      icon.toLowerCase().includes(normalizedKeyword),
    );
  }, [keyword]);

  return (
    <Flex vertical gap="small" style={{ width: '100%' }}>
      <Input
        allowClear
        placeholder="搜索 Outlined 图标"
        value={keyword}
        onChange={(event) => setKeyword(event.target.value)}
      />
      {value && (
        <Space size="small">
          {selectedIcon && React.createElement(selectedIcon)}
          <Tag
            closable
            onClose={(event) => {
              event.preventDefault();
              onChange?.(undefined);
            }}
          >
            {getIconLabel(value)}
          </Tag>
        </Space>
      )}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(96px, 1fr))',
          gap: 8,
          maxHeight: 320,
          overflowY: 'auto',
          padding: 8,
          border: '1px solid #d9d9d9',
          borderRadius: 6,
        }}
      >
        {filteredIconNames.map((icon) => {
          const Icon = getAntIcon(icon);
          const label = getIconLabel(icon);
          if (!Icon) {
            return null;
          }
          return (
            <Tooltip key={icon} title={label}>
              <Button
                style={{ height: 72, padding: 6, width: '100%' }}
                type={value === icon ? 'primary' : 'default'}
                onClick={() => onChange?.(icon)}
              >
                <Flex
                  vertical
                  align="center"
                  justify="center"
                  gap={4}
                  style={{ height: '100%', width: '100%' }}
                >
                  <Icon style={{ fontSize: 24 }} />
                  <span
                    style={{
                      display: 'block',
                      overflow: 'hidden',
                      textOverflow: 'ellipsis',
                      whiteSpace: 'nowrap',
                      width: '100%',
                    }}
                  >
                    {label}
                  </span>
                </Flex>
              </Button>
            </Tooltip>
          );
        })}
      </div>
    </Flex>
  );
};

export default IconPicker;
