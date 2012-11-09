package spockintegration

import grails.plugin.spock.IntegrationSpec

public class UserControllerIntegrationSpec extends IntegrationSpec {

    UserController controller = new UserController()
    Map redirectArgs
    Map renderArgs

    def setup() {
        controller.metaClass.redirect = {Map map ->
            redirectArgs = map
        }
        controller.metaClass.render = {Map map ->
            renderArgs = map
        }
    }

    def "account is created whenever user is created"() {
        setup:
        Integer userCountBefore = User.count()
        Integer accountCountBefore = Account.count()

        controller.params.userName = "Test"
        controller.params.password = "Test"

        when:
        controller.save()

        then:
        (User.count() - userCountBefore) == 1
        (Account.count() - accountCountBefore) == 1
        redirectArgs.action == "show"
    }


    def "user not created for null username"() {
        setup:
        Integer userCountBefore = User.count()
        Integer accountCountBefore = Account.count()
        controller.params.password = "Test"

        when:
        controller.save()

        then:
        (User.count() - userCountBefore) == 0
        (Account.count() - accountCountBefore) == 0
        renderArgs.view == "create"
    }

    def "test json response for user"() {
        setup:
        User user = new User(password: "Test", userName: "Test")
        user.save(flush: true)

        when:
        controller.params.id = user.id
        controller.showJson()

        then:
        controller.response.contentAsString.contains('"class":"spockintegration.User"')
    }

    def "test string response for user"() {
        setup:
        User user = new User(password: "Test", userName: "Test")
        user.save(flush: true)

        when:
        controller.params.id = user.id
        controller.showName()

        then:
        controller.response.contentAsString == 'Test'

    }

    def "test xml response for user"() {     //EXERCISE
        setup:
        User user = new User(password: "Test", userName: "Test")
        user.save(flush: true)

        when:
        controller.params.id = user.id
        controller.showXml()

        then:
        controller.response.contentAsString.contains('''<password>Test</password><userName>Test</userName></user>''')

    }

}