import {callback} from '../shared/build/dist/js/developmentLibrary/GatherSpace-shared.mjs';

export function setupCounter(element) {
  callback((e) => {
    console.log(`Logging ${e}`);
  })

  let counter = 0
  const setCounter = (count) => {
    counter = count
    element.innerHTML = `count is ${counter}`
  }
  element.addEventListener('click', () => setCounter(counter + 1))
  setCounter(0)
}
