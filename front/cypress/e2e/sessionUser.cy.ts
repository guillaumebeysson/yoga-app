
/// <reference types="cypress" />

describe('Session spec', () => {

    const session1a = {
        id: 1,
        name: "Dance",
        description: 'Cours de dance',
        date: '2024-07-22',
        teacher_id: 1,
        users: [1, 2],
        createdAt: '2024-01-12',
        updatedAt: '2024-07-12',
    }

    const session1b = {
        id: 1,
        name: "Dance",
        description: 'Cours de dance',
        date: '2024-07-21',
        teacher_id: 1,
        users: [2],
        createdAt: '2024-07-08',
        updatedAt: '2024-07-08',
    }

    const session2 = {
        id: 2,
        name: "Music",
        description: 'Cours de music',
        date: '2024-07-01',
        teacher_id: 2,
        users: [2, 3, 5],
        createdAt: '2024-07-15',
        updatedAt: '2024-07-15',
    }

    const teacher1 = {
        id: 1,
        lastName: 'DELAHAYE',
        firstName: 'Margot',
        createdAt: '2024-03-12',
        updatedAt: '2024-03-12',
    }

    it('User log', () => {
        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'jm.sandale@gmail.com',
                firstName: 'Jean-Michel',
                lastName: 'Sandale',
                admin: false
            },
        })
            .as('SessionInformation')

        cy.intercept('GET', '/api/session', [session1a, session2])
            .as('Session List')

        cy.get('input[formControlName=email]').type("jm.sandale@gmail.com")
        cy.get('input[formControlName=password]').type(`${"Coucou12@"}{enter}{enter}`)

        cy.url().should('include', '/sessions')
    })

    it('Not participate', () => {
        cy.intercept('GET', '/api/session/1', session1a)
            .as('Session')

        cy.intercept('GET', '/api/teacher/1', teacher1)
            .as('Teacher')

        cy.intercept('DELETE', '/api/session/1/participate/1', {})
            .as('Void')

        cy.contains('Detail').first().click()

        cy.url().should('include', '/sessions/detail/1')
        cy.should('not.contain', 'Detail')

        cy.intercept('GET', '/api/session/1', session1b)
            .as('Session')

        cy.contains('Do not participate').click()

        cy.contains('Participate')
    })



    it('Participate', () => {
        cy.intercept('POST', '/api/session/1/participate/1', {})
            .as('Void')

        cy.intercept('GET', '/api/session/1', session1a)
            .as('Session')

        cy.intercept('GET', '/api/teacher/1', teacher1)
            .as('Teacher')

        cy.contains('Participate').click()

        cy.contains('Do not participate')
    })

});
