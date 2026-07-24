const numberFormatter = new Intl.NumberFormat('en-US');
const amountFormatter = new Intl.NumberFormat('en-US', {
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
});

/**
 * Format a number with thousand separators.
 * Replaces numeral(val).format('0,0')
 */
export const formatNumber = (val: number | string): string => {
  const parsed = Number(val);
  return Number.isFinite(parsed) ? numberFormatter.format(parsed) : '';
};

/**
 * Format an amount with thousand separators and two decimal places.
 */
export const formatAmount = (val?: number | string | null): string => {
  const parsed = Number(val ?? 0);
  return Number.isFinite(parsed) ? amountFormatter.format(parsed) : '';
};

/**
 * Format a number as yuan currency string.
 * Replaces `¥ ${numeral(val).format('0,0')}`
 */
export const formatYuan = (val: number | string) => `¥ ${formatNumber(val)}`;
