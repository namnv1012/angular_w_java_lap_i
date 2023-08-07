export function pagination(currentPage, totalPage) {
  const delta = 2;
  const left = currentPage - delta;
  const right = currentPage + delta + 1;
  const range = [];
  const rangeWithDots = [];
  let l;

  for (let i = 1; i <= totalPage; i++) {
    if (i === 1 || i === totalPage || (i >= left && i < right)) {
      range.push(i);
    }
  }

  for (const i of range) {
    if (l) {
      if (i - l === 2) {
        rangeWithDots.push(l + 1);
      } else if (i - l !== 1) {
        rangeWithDots.push('...');
      }
    }
    rangeWithDots.push(i);
    l = i;
  }

  return rangeWithDots;
}

export function next(currentPage, totalPage) {
  currentPage++;

  if (currentPage > totalPage) {
    currentPage = totalPage;
    return;
  }

  return currentPage;
}

export function prev(currentPage) {
  currentPage--;

  if (currentPage < 1) {
    currentPage = 1;
    return;
  }

  return currentPage;
}
