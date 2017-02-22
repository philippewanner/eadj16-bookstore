import { NgBookstorePage } from './app.po';

describe('ng-bookstore App', function() {
  let page: NgBookstorePage;

  beforeEach(() => {
    page = new NgBookstorePage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
