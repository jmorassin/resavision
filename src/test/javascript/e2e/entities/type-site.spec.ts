import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('TypeSite e2e test', () => {

    let navBarPage: NavBarPage;
    let typeSiteDialogPage: TypeSiteDialogPage;
    let typeSiteComponentsPage: TypeSiteComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load TypeSites', () => {
        navBarPage.goToEntity('type-site');
        typeSiteComponentsPage = new TypeSiteComponentsPage();
        expect(typeSiteComponentsPage.getTitle()).toMatch(/resavisionApp.typeSite.home.title/);

    });

    it('should load create TypeSite dialog', () => {
        typeSiteComponentsPage.clickOnCreateButton();
        typeSiteDialogPage = new TypeSiteDialogPage();
        expect(typeSiteDialogPage.getModalTitle()).toMatch(/resavisionApp.typeSite.home.createOrEditLabel/);
        typeSiteDialogPage.close();
    });

    it('should create and save TypeSites', () => {
        typeSiteComponentsPage.clickOnCreateButton();
        typeSiteDialogPage.setNomInput('nom');
        expect(typeSiteDialogPage.getNomInput()).toMatch('nom');
        typeSiteDialogPage.save();
        expect(typeSiteDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TypeSiteComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-type-site div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TypeSiteDialogPage {
    modalTitle = element(by.css('h4#myTypeSiteLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nomInput = element(by.css('input#field_nom'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNomInput = function (nom) {
        this.nomInput.sendKeys(nom);
    }

    getNomInput = function () {
        return this.nomInput.getAttribute('value');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
