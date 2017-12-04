import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Responsable e2e test', () => {

    let navBarPage: NavBarPage;
    let responsableDialogPage: ResponsableDialogPage;
    let responsableComponentsPage: ResponsableComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Responsables', () => {
        navBarPage.goToEntity('responsable');
        responsableComponentsPage = new ResponsableComponentsPage();
        expect(responsableComponentsPage.getTitle()).toMatch(/resavisionApp.responsable.home.title/);

    });

    it('should load create Responsable dialog', () => {
        responsableComponentsPage.clickOnCreateButton();
        responsableDialogPage = new ResponsableDialogPage();
        expect(responsableDialogPage.getModalTitle()).toMatch(/resavisionApp.responsable.home.createOrEditLabel/);
        responsableDialogPage.close();
    });

    it('should create and save Responsables', () => {
        responsableComponentsPage.clickOnCreateButton();
        responsableDialogPage.civiliteSelectLastOption();
        responsableDialogPage.getCheckTelInput().isSelected().then(function (selected) {
            if (selected) {
                responsableDialogPage.getCheckTelInput().click();
                expect(responsableDialogPage.getCheckTelInput().isSelected()).toBeFalsy();
            } else {
                responsableDialogPage.getCheckTelInput().click();
                expect(responsableDialogPage.getCheckTelInput().isSelected()).toBeTruthy();
            }
        });
        responsableDialogPage.getChekMailInput().isSelected().then(function (selected) {
            if (selected) {
                responsableDialogPage.getChekMailInput().click();
                expect(responsableDialogPage.getChekMailInput().isSelected()).toBeFalsy();
            } else {
                responsableDialogPage.getChekMailInput().click();
                expect(responsableDialogPage.getChekMailInput().isSelected()).toBeTruthy();
            }
        });
        responsableDialogPage.userSelectLastOption();
        responsableDialogPage.boutiqueSelectLastOption();
        responsableDialogPage.save();
        expect(responsableDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ResponsableComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-responsable div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ResponsableDialogPage {
    modalTitle = element(by.css('h4#myResponsableLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    civiliteSelect = element(by.css('select#field_civilite'));
    checkTelInput = element(by.css('input#field_checkTel'));
    chekMailInput = element(by.css('input#field_chekMail'));
    userSelect = element(by.css('select#field_user'));
    boutiqueSelect = element(by.css('select#field_boutique'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCiviliteSelect = function (civilite) {
        this.civiliteSelect.sendKeys(civilite);
    }

    getCiviliteSelect = function () {
        return this.civiliteSelect.element(by.css('option:checked')).getText();
    }

    civiliteSelectLastOption = function () {
        this.civiliteSelect.all(by.tagName('option')).last().click();
    }
    getCheckTelInput = function () {
        return this.checkTelInput;
    }
    getChekMailInput = function () {
        return this.chekMailInput;
    }
    userSelectLastOption = function () {
        this.userSelect.all(by.tagName('option')).last().click();
    }

    userSelectOption = function (option) {
        this.userSelect.sendKeys(option);
    }

    getUserSelect = function () {
        return this.userSelect;
    }

    getUserSelectedOption = function () {
        return this.userSelect.element(by.css('option:checked')).getText();
    }

    boutiqueSelectLastOption = function () {
        this.boutiqueSelect.all(by.tagName('option')).last().click();
    }

    boutiqueSelectOption = function (option) {
        this.boutiqueSelect.sendKeys(option);
    }

    getBoutiqueSelect = function () {
        return this.boutiqueSelect;
    }

    getBoutiqueSelectedOption = function () {
        return this.boutiqueSelect.element(by.css('option:checked')).getText();
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
