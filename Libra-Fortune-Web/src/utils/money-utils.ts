export function getMoneySymbol(currencyType: string): string {
  switch (currencyType) {
    case 'CNY':
      return '¥';
    case 'USD':
      return '$';
    case 'EUR':
      return '€';
    case 'JPY':
      return '日圆';
    case 'SGD':
      return 'S$';
  }
  return currencyType;
}
