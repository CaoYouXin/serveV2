import { BlogConsolePage } from './app.po';

describe('blog-console App', function() {
  let page: BlogConsolePage;

  beforeEach(() => {
    page = new BlogConsolePage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
