/// <reference types="cypress" />

describe('Session spec', () => {

    const session1 = {
        id: 1,
        name: "Dance",
        description: 'Cours de dance',
        date: '2024-07-22',
        teacher_id: 1,
        users: [1, 2],
        createdAt: '2024-01-12',
        updatedAt: '2024-07-12',
    }

    const session2 = {
        id: 2,
        name: "Méditation",
        description: 'Cours de méditation',
        date: '2024-07-19',
        teacher_id: 2,
        users: [2, 3, 5],
        createdAt: '2024-07-18',
        updatedAt: '2024-07-18',
    }

    const session2Update = {
        id: 2,
        name: 'Souplesse',
        description: 'Cours de souplesse',
        date: '2024-05-09',
        teacher_id: 1,
        users: [],
        createdAt: '2024-05-08',
        updatedAt: '2024-05-08',
    }

    const session3 = {
        id: 3,
        name: 'Stretching',
        description: 'Cours de stretching',
        date: '2024-07-03',
        teacher_id: 1,
        users: [],
        createdAt: '2024-06-12',
        updatedAt: '2024-06-12',
    }

    const teacher1 = {
        id: 1,
        lastName: 'THIERCELIN',
        firstName: 'Hélène',
        createdAt: '2024-04-20',
        updatedAt: '2024-04-20',
    }

    const teacher2 = {
        id: 2,
        lastName: 'DELAHAYE',
        firstName: 'Margot',
        createdAt: '2024-04-24',
        updatedAt: '2023-04-24',
    }

    it('Admin log', () => {
        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'yoga@studio.com',
                firstName: 'Guillaume',
                lastName: 'Beysson',
                admin: true
            },
        })
            .as('SessionInformation')

        cy.intercept('GET', '/api/session', [session1, session2])
            .as('Session List')

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')
    })

    it('Display session list', () => {
        cy.contains('Rentals available')
        cy.contains(session1.name)
        cy.contains(session1.description)
        cy.contains(session2.name)
        cy.contains(session1.description)
    })

    it('Display session details', () => {
        cy.intercept('GET', '/api/session/1', session1)
            .as('Session')

        cy.intercept('GET', '/api/teacher/1', teacher1)
            .as('Teacher')

        cy.contains('Detail').first().click()

        cy.url().should('include', '/sessions/detail/1')
        cy.contains(session1.name)
        cy.contains(session1.description)
        cy.contains(`${session1.users.length} attendees`)
        cy.contains(teacher1.firstName)
        cy.contains(teacher1.lastName)
    })

    it('Delete session', () => {
        cy.intercept('DELETE', '/api/session/1', {})
            .as('Any')

        cy.intercept('GET', '/api/session', [session2])
            .as('Session List')

        cy.contains('Delete').click()
        cy.contains('Session deleted !')
        cy.contains('Close').click()

        cy.url().should('include', '/sessions')
    })

    it('Create session', () => {

        cy.intercept('GET', '/api/teacher', [teacher1, teacher2])
            .as('Teacher List')

        cy.contains('Create').click()

        cy.contains('Create session')

        cy.get('input[formControlName=name]').type(session3.name)
        cy.get('input[formControlName=date]').type(session3.date)
        cy.get('mat-select[formControlName=teacher_id]').click()
        cy.contains('Margot DELAHAYE').click()
        cy.contains('Save').should('be.disabled')
        cy.get('textarea[formControlName=description]').type(session3.description)

        cy.intercept('POST', '/api/session', session3)
            .as('Session')

        cy.intercept('GET', '/api/session', [session2, session3])
            .as('Session List')

        cy.contains('Save').click()

        cy.contains('Session created !')
        cy.contains('Close').click()
        cy.url().should('include', '/sessions')

    })

    it('Update Session', () => {

        cy.intercept('GET', '/api/session/2', session2)
            .as('Session')

        cy.intercept('GET', '/api/teacher', [teacher1, teacher2])
            .as('Teacher List')

        cy.contains('Edit').first().click()

        cy.url().should('include', '/sessions/update/2')
        cy.contains('Update session')

        cy.get('input[formControlName=name]').should('have.value', session2.name).clear().type(session2Update.name)
        cy.get('textarea[formControlName=description]').should('have.value', session2.description).clear().type(session2Update.description)

        cy.intercept('PUT', '/api/session/2', session2Update)
            .as('Session')

        cy.intercept('GET', '/api/session', [session2Update, session3])
            .as('Session List')

        cy.contains('Save').click()

        cy.contains('Session updated !')
        cy.contains('Close').click()
        cy.url().should('include', '/sessions')
    })

});