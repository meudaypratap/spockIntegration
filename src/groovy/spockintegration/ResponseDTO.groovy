package spockintegration;

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass')
class ResponseDTO {
    Boolean status
    String message
}
