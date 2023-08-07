export class GetCatalog {
  static readonly type = '[CatalogModel] Get'

  constructor(public categoryCode: string) {
  }
}
