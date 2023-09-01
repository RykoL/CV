const getAllDetailElements = () => document.querySelectorAll("details")
const toggleDetailElementToOpen = (summaryElement) => {
  summaryElement.open = true;
}

export const isMediaTypePrint = () => window.matchMedia("print").matches
export const toggleDetailElementsOpen = () => {
  getAllDetailElements().forEach(toggleDetailElementToOpen)
}
