import { BlogConsolePage } from './app.po';

describe('blog-console App', () => {
  let page: BlogConsolePage;

  beforeEach(() => {
    page = new BlogConsolePage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
