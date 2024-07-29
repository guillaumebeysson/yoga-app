/// <reference types="cypress" />

describe('Register spec', () => {

    it('Register user', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', {})

        cy.get('input[formControlName=firstName]').type("TestFistName")
        cy.get('input[formControlName=lastName]').type("TestLastName")
        cy.get('input[formControlName=email]').type("guillaume.beysson@gmail.com")
        cy.get('input[formControlName=password]').type(`${"Coucou12@"}{enter}{enter}`)

        cy.url().should('include', '/login')
    })

    it('Error Submit button disabled if password is empty', () => {
        cy.visit('/register')
        cy.get('input[formControlName=firstName]').type("TestFistName")
        cy.get('input[formControlName=lastName]').type("TestLastName")
        cy.get('input[formControlName=email]').type("guillaume.beysson@gmail.com")
        cy.contains('Submit').should('be.disabled')
    })

    it('Error Submit button disabled if email is empty', () => {
        cy.visit('/register')
        cy.get('input[formControlName=firstName]').type("TestFistName")
        cy.get('input[formControlName=lastName]').type("TestLastName")
        cy.get('input[formControlName=password]').type("Coucou12@")
        cy.contains('Submit').should('be.disabled')
    })

    it('Error Submit button disabled if firstname is empty', () => {
        cy.visit('/register')
        cy.get('input[formControlName=lastName]').type("TestLastName")
        cy.get('input[formControlName=email]').type("guillaume.beysson@gmail.com")
        cy.get('input[formControlName=password]').type("Coucou12@")
        cy.contains('Submit').should('be.disabled')
    })

    it('Error Submit button disabled if lastname is empty', () => {
        cy.visit('/register')
        cy.get('input[formControlName=firstName]').type("TestFistName")
        cy.get('input[formControlName=email]').type("guillaume.beysson@gmail.com")
        cy.get('input[formControlName=password]').type("Coucou12@")
        cy.contains('Submit').should('be.disabled')
    })

    it('Error Submit button disabled if email is not a valid email', () => {
        cy.visit('/register')
        cy.get('input[formControlName=firstName]').type("TestFistName")
        cy.get('input[formControlName=lastName]').type("TestLastName")
        cy.get('input[formControlName=email]').type("guillaume.beysson")
        cy.get('input[formControlName=password]').type("Coucou12@")
        cy.contains('Submit').should('be.disabled')
    })

    it('Error if email already exists', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', { statusCode: 400 })

        cy.get('input[formControlName=firstName]').type("TestFistName")
        cy.get('input[formControlName=lastName]').type("TestLastName")
        cy.get('input[formControlName=email]').type("guillaume.beysson@gmail.com")
        cy.get('input[formControlName=password]').type("Coucou12@")
        cy.contains('Submit').click()
        cy.contains('An error occurred')
    })

});