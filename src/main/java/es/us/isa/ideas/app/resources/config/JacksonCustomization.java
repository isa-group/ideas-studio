package es.us.isa.ideas.app.resources.config;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 *
 * @author japarejo
 */
 
public class JacksonCustomization {           
     
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    
    private CustomJacksonObjectMapper objectMapper;
/*
    @PostConstruct
    public void init() {
        List<HttpMessageConverter<?>> messageConverters = requestMappingHandlerAdapter.getMessageConverters();
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            if (messageConverter instanceof MappingJacksonHttpMessageConverter) {
                MappingJacksonHttpMessageConverter m = (MappingJacksonHttpMessageConverter) messageConverter;
                m.setObjectMapper(objectMapper);
            }
        }
    }

    // this will exist due to the <mvc:annotation-driven/> bean
    @Autowired
    public void setRequestMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    @Autowired
    public void setObjectMapper(CustomJacksonObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }   */
}
