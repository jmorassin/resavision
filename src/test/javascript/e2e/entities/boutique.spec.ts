import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Boutique e2e test', () => {

    let navBarPage: NavBarPage;
    let boutiqueDialogPage: BoutiqueDialogPage;
    let boutiqueComponentsPage: BoutiqueComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Boutiques', () => {
        navBarPage.goToEntity('boutique');
        boutiqueComponentsPage = new BoutiqueComponentsPage();
        expect(boutiqueComponentsPage.getTitle()).toMatch(/resavisionApp.boutique.home.title/);

    });

    it('should load create Boutique dialog', () => {
        boutiqueComponentsPage.clickOnCreateButton();
        boutiqueDialogPage = new BoutiqueDialogPage();
        expect(boutiqueDialogPage.getModalTitle()).toMatch(/resavisionApp.boutique.home.createOrEditLabel/);
        boutiqueDialogPage.close();
    });

    it('should create and save Boutiques', () => {
        boutiqueComponentsPage.clickOnCreateButton();
        boutiqueDialogPage.setNomInput('nom');
        expect(boutiqueDialogPage.getNomInput()).toMatch('nom');
        boutiqueDialogPage.setLogoInput(absolutePath);
        boutiqueDialogPage.setUrlInput('url');
        expect(boutiqueDialogPage.getUrlInput()).toMatch('url');
        boutiqueDialogPage.setNumTelephoneInput('numTelephone');
        expect(boutiqueDialogPage.getNumTelephoneInput()).toMatch('numTelephone');
        boutiqueDialogPage.setMailInput('mail');
        expect(boutiqueDialogPage.getMailInput()).toMatch('mail');
        boutiqueDialogPage.setAdresse1Input('adresse1');
        expect(boutiqueDialogPage.getAdresse1Input()).toMatch('adresse1');
        boutiqueDialogPage.setAdresse2Input('adresse2');
        expect(boutiqueDialogPage.getAdresse2Input()).toMatch('adresse2');
        boutiqueDialogPage.villeSelectLastOption();
        // boutiqueDialogPage.typeBoutiqueSelectLastOption();
        boutiqueDialogPage.save();
        expect(boutiqueDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class BoutiqueComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-boutique div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class BoutiqueDialogPage {
    modalTitle = element(by.css('h4#myBoutiqueLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nomInput = element(by.css('input#field_nom'));
    logoInput = element(by.css('input#file_logo'));
    urlInput = element(by.css('input#field_url'));
    numTelephoneInput = element(by.css('input#field_numTelephone'));
    mailInput = element(by.css('input#field_mail'));
    adresse1Input = element(by.css('input#field_adresse1'));
    adresse2Input = element(by.css('input#field_adresse2'));
    villeSelect = element(by.css('select#field_ville'));
    typeBoutiqueSelect = element(by.css('select#field_typeBoutique'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNomInput = function (nom) {
        this.nomInput.sendKeys(nom);
    }

    getNomInput = function () {
        return this.nomInput.getAttribute('value');
    }

    setLogoInput = function (logo) {
        this.logoInput.sendKeys(logo);
    }

    getLogoInput = function () {
        return this.logoInput.getAttribute('value');
    }

    setUrlInput = function (url) {
        this.urlInput.sendKeys(url);
    }

    getUrlInput = function () {
        return this.urlInput.getAttribute('value');
    }

    setNumTelephoneInput = function (numTelephone) {
        this.numTelephoneInput.sendKeys(numTelephone);
    }

    getNumTelephoneInput = function () {
        return this.numTelephoneInput.getAttribute('value');
    }

    setMailInput = function (mail) {
        this.mailInput.sendKeys(mail);
    }

    getMailInput = function () {
        return this.mailInput.getAttribute('value');
    }

    setAdresse1Input = function (adresse1) {
        this.adresse1Input.sendKeys(adresse1);
    }

    getAdresse1Input = function () {
        return this.adresse1Input.getAttribute('value');
    }

    setAdresse2Input = function (adresse2) {
        this.adresse2Input.sendKeys(adresse2);
    }

    getAdresse2Input = function () {
        return this.adresse2Input.getAttribute('value');
    }

    villeSelectLastOption = function () {
        this.villeSelect.all(by.tagName('option')).last().click();
    }

    villeSelectOption = function (option) {
        this.villeSelect.sendKeys(option);
    }

    getVilleSelect = function () {
        return this.villeSelect;
    }

    getVilleSelectedOption = function () {
        return this.villeSelect.element(by.css('option:checked')).getText();
    }

    typeBoutiqueSelectLastOption = function () {
        this.typeBoutiqueSelect.all(by.tagName('option')).last().click();
    }

    typeBoutiqueSelectOption = function (option) {
        this.typeBoutiqueSelect.sendKeys(option);
    }

    getTypeBoutiqueSelect = function () {
        return this.typeBoutiqueSelect;
    }

    getTypeBoutiqueSelectedOption = function () {
        return this.typeBoutiqueSelect.element(by.css('option:checked')).getText();
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
