import { history } from '@umijs/max';

export function appendSearchParams(params: Record<string, string>): string {
  const urlSearchParams = new URLSearchParams(history.location.search);
  Object.entries(params).forEach(([key, value]) => {
    urlSearchParams.append(key, value);
  });
  return urlSearchParams.toString();
}
