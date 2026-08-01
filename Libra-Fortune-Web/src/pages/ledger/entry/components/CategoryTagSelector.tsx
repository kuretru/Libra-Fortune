import { Space, Tag } from 'antd';
import React, { useEffect, useState } from 'react';
import type { CategorySelectorValue } from './types';

type CategoryTagSelectorProps = {
  categories: GalaxyWeb.EnumDTO<number>[];
  value?: CategorySelectorValue;
  onChange?: (value?: CategorySelectorValue) => void;
};

const findParentCategory = (
  categories: GalaxyWeb.EnumDTO<number>[],
  categoryIdL1?: number,
) => categories.find((category) => category.value === categoryIdL1);

const CategoryTagSelector: React.FC<CategoryTagSelectorProps> = ({
  categories,
  value,
  onChange,
}) => {
  const [selectedParentId, setSelectedParentId] = useState<number | undefined>(
    () => findParentCategory(categories, value?.categoryIdL1)?.value,
  );

  useEffect(() => {
    const parent = findParentCategory(categories, value?.categoryIdL1);
    if (parent) {
      setSelectedParentId(parent.value);
      return;
    }
    setSelectedParentId((oldParentId) =>
      oldParentId &&
      categories.some((category) => category.value === oldParentId)
        ? oldParentId
        : undefined,
    );
  }, [categories, value]);

  const selectedParent = findParentCategory(categories, selectedParentId);

  const selectParent = (category: GalaxyWeb.EnumDTO<number>) => {
    setSelectedParentId(category.value);
    if (value?.categoryIdL1 !== category.value) {
      onChange?.(undefined);
    }
  };

  return (
    <Space vertical size={8}>
      <Space size={[0, 8]} wrap>
        {categories.map((category) => (
          <Tag.CheckableTag
            key={category.value}
            checked={selectedParentId === category.value}
            onChange={() => selectParent(category)}
          >
            {category.label}
          </Tag.CheckableTag>
        ))}
      </Space>
      {selectedParent?.children?.length ? (
        <Space size={[0, 8]} wrap>
          {selectedParent.children.map((category) => (
            <Tag.CheckableTag
              key={category.value}
              checked={value?.categoryIdL2 === category.value}
              onChange={() =>
                onChange?.({
                  categoryIdL1: selectedParent.value,
                  categoryIdL2: category.value,
                })
              }
            >
              {category.label}
            </Tag.CheckableTag>
          ))}
        </Space>
      ) : null}
    </Space>
  );
};

export default CategoryTagSelector;
