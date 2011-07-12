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
        controller.params.password = "Test"

        when:
        controller.save()

        then:
        renderArgs.view == "create"
    }


}