package com.sasagui.onmangequoi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest
class MvcSpecification extends OnMangeQuoiSpec {

    @Autowired
    protected MockMvc mvc
}
