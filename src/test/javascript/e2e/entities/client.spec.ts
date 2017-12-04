import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Client e2e test', () => {

    let navBarPage: NavBarPage;
    let clientDialogPage: ClientDialogPage;
    let clientComponentsPage: ClientComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Clients', () => {
        navBarPage.goToEntity('client');
        clientComponentsPage = new ClientComponentsPage();
        expect(clientComponentsPage.getTitle()).toMatch(/resavisionApp.client.home.title/);

    });

    it('should load create Client dialog', () => {
        clientComponentsPage.clickOnCreateButton();
        clientDialogPage = new ClientDialogPage();
        expect(clientDialogPage.getModalTitle()).toMatch(/resavisionApp.client.home.createOrEditLabel/);
        clientDialogPage.close();
    });

    it('should create and save Clients', () => {
        clientComponentsPage.clickOnCreateButton();
        clientDialogPage.civiliteSelectLastOption();
        clientDialogPage.getCheckTelInput().isSelected().then(function (selected) {
            if (selected) {
                clientDialogPage.getCheckTelInput().click();
                expect(clientDialogPage.getCheckTelInput().isSelected()).toBeFalsy();
            } else {
                clientDialogPage.getCheckTelInput().click();
                expect(clientDialogPage.getCheckTelInput().isSelected()).toBeTruthy();
            }
        });
        clientDialogPage.getChekMailInput().isSelected().then(function (selected) {
            if (selected) {
                clientDialogPage.getChekMailInput().click();
                expect(clientDialogPage.getChekMailInput().isSelected()).toBeFalsy();
            } else {
                clientDialogPage.getChekMailInput().click();
                expect(clientDialogPage.getChekMailInput().isSelected()).toBeTruthy();
            }
        });
        clientDialogPage.userSelectLastOption();
        clientDialogPage.save();
        expect(clientDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ClientComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-client div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ClientDialogPage {
    modalTitle = element(by.css('h4#myClientLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    civiliteSelect = element(by.css('select#field_civilite'));
    checkTelInput = element(by.css('input#field_checkTel'));
    chekMailInput = element(by.css('input#field_chekMail'));
    userSelect = element(by.css('select#field_user'));

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
