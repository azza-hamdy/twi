import { DexcomPortalPage } from './app.po';

describe('dexcom-portal App', () => {
  let page: DexcomPortalPage;

  beforeEach(() => {
    page = new DexcomPortalPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
