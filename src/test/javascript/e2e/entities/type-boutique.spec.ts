import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('TypeBoutique e2e test', () => {

    let navBarPage: NavBarPage;
    let typeBoutiqueDialogPage: TypeBoutiqueDialogPage;
    let typeBoutiqueComponentsPage: TypeBoutiqueComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load TypeBoutiques', () => {
        navBarPage.goToEntity('type-boutique');
        typeBoutiqueComponentsPage = new TypeBoutiqueComponentsPage();
        expect(typeBoutiqueComponentsPage.getTitle()).toMatch(/resavisionApp.typeBoutique.home.title/);

    });

    it('should load create TypeBoutique dialog', () => {
        typeBoutiqueComponentsPage.clickOnCreateButton();
        typeBoutiqueDialogPage = new TypeBoutiqueDialogPage();
        expect(typeBoutiqueDialogPage.getModalTitle()).toMatch(/resavisionApp.typeBoutique.home.createOrEditLabel/);
        typeBoutiqueDialogPage.close();
    });

    it('should create and save TypeBoutiques', () => {
        typeBoutiqueComponentsPage.clickOnCreateButton();
        typeBoutiqueDialogPage.setNomInput('nom');
        expect(typeBoutiqueDialogPage.getNomInput()).toMatch('nom');
        typeBoutiqueDialogPage.typeSiteSelectLastOption();
        typeBoutiqueDialogPage.save();
        expect(typeBoutiqueDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TypeBoutiqueComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-type-boutique div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TypeBoutiqueDialogPage {
    modalTitle = element(by.css('h4#myTypeBoutiqueLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nomInput = element(by.css('input#field_nom'));
    typeSiteSelect = element(by.css('select#field_typeSite'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNomInput = function (nom) {
        this.nomInput.sendKeys(nom);
    }

    getNomInput = function () {
        return this.nomInput.getAttribute('value');
    }

    typeSiteSelectLastOption = function () {
        this.typeSiteSelect.all(by.tagName('option')).last().click();
    }

    typeSiteSelectOption = function (option) {
        this.typeSiteSelect.sendKeys(option);
    }

    getTypeSiteSelect = function () {
        return this.typeSiteSelect;
    }

    getTypeSiteSelectedOption = function () {
        return this.typeSiteSelect.element(by.css('option:checked')).getText();
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
