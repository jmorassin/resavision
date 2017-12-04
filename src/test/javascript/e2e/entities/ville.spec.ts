import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Ville e2e test', () => {

    let navBarPage: NavBarPage;
    let villeDialogPage: VilleDialogPage;
    let villeComponentsPage: VilleComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Villes', () => {
        navBarPage.goToEntity('ville');
        villeComponentsPage = new VilleComponentsPage();
        expect(villeComponentsPage.getTitle()).toMatch(/resavisionApp.ville.home.title/);

    });

    it('should load create Ville dialog', () => {
        villeComponentsPage.clickOnCreateButton();
        villeDialogPage = new VilleDialogPage();
        expect(villeDialogPage.getModalTitle()).toMatch(/resavisionApp.ville.home.createOrEditLabel/);
        villeDialogPage.close();
    });

    it('should create and save Villes', () => {
        villeComponentsPage.clickOnCreateButton();
        villeDialogPage.setNomInput('nom');
        expect(villeDialogPage.getNomInput()).toMatch('nom');
        villeDialogPage.setCodePostalInput('codePostal');
        expect(villeDialogPage.getCodePostalInput()).toMatch('codePostal');
        villeDialogPage.save();
        expect(villeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class VilleComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-ville div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class VilleDialogPage {
    modalTitle = element(by.css('h4#myVilleLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nomInput = element(by.css('input#field_nom'));
    codePostalInput = element(by.css('input#field_codePostal'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNomInput = function (nom) {
        this.nomInput.sendKeys(nom);
    }

    getNomInput = function () {
        return this.nomInput.getAttribute('value');
    }

    setCodePostalInput = function (codePostal) {
        this.codePostalInput.sendKeys(codePostal);
    }

    getCodePostalInput = function () {
        return this.codePostalInput.getAttribute('value');
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
