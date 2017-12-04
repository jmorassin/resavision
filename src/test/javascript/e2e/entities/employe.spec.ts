import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Employe e2e test', () => {

    let navBarPage: NavBarPage;
    let employeDialogPage: EmployeDialogPage;
    let employeComponentsPage: EmployeComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Employes', () => {
        navBarPage.goToEntity('employe');
        employeComponentsPage = new EmployeComponentsPage();
        expect(employeComponentsPage.getTitle()).toMatch(/resavisionApp.employe.home.title/);

    });

    it('should load create Employe dialog', () => {
        employeComponentsPage.clickOnCreateButton();
        employeDialogPage = new EmployeDialogPage();
        expect(employeDialogPage.getModalTitle()).toMatch(/resavisionApp.employe.home.createOrEditLabel/);
        employeDialogPage.close();
    });

    it('should create and save Employes', () => {
        employeComponentsPage.clickOnCreateButton();
        employeDialogPage.civiliteSelectLastOption();
        employeDialogPage.getCheckTelInput().isSelected().then(function (selected) {
            if (selected) {
                employeDialogPage.getCheckTelInput().click();
                expect(employeDialogPage.getCheckTelInput().isSelected()).toBeFalsy();
            } else {
                employeDialogPage.getCheckTelInput().click();
                expect(employeDialogPage.getCheckTelInput().isSelected()).toBeTruthy();
            }
        });
        employeDialogPage.getChekMailInput().isSelected().then(function (selected) {
            if (selected) {
                employeDialogPage.getChekMailInput().click();
                expect(employeDialogPage.getChekMailInput().isSelected()).toBeFalsy();
            } else {
                employeDialogPage.getChekMailInput().click();
                expect(employeDialogPage.getChekMailInput().isSelected()).toBeTruthy();
            }
        });
        employeDialogPage.userSelectLastOption();
        employeDialogPage.boutiqueSelectLastOption();
        employeDialogPage.save();
        expect(employeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EmployeComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-employe div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EmployeDialogPage {
    modalTitle = element(by.css('h4#myEmployeLabel'));
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
