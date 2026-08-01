import type { Dayjs } from 'dayjs';

export type CategorySelectorValue = {
  categoryIdL1?: number;
  categoryIdL2?: number;
};

export type LedgerEntryDetailFormValues =
  Partial<LibraFortune.Ledger.LedgerEntryDetailDTO>;

export type LedgerEntryFormValues = Omit<
  Partial<LibraFortune.Ledger.LedgerEntryDTO>,
  'date' | 'details' | 'tags'
> & {
  categoryIds?: CategorySelectorValue;
  date?: string | Dayjs;
  details?: LedgerEntryDetailFormValues[];
  tagIds?: number[];
};

export type Option<T extends string | number = string | number> = {
  label: string;
  value: T;
};

export type GroupedTagOption = {
  allowMultiple: boolean;
  label: string;
  name: string;
  options: Option<number>[];
  required: boolean;
  value: number;
};

export type DetailLockTypes = {
  unlock: string;
  ratio: string;
  amount: string;
};
