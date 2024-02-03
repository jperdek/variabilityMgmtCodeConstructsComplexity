export interface Decorator {
  setDecorated(decorated: boolean): void;
  getDecorated(): boolean;
}
