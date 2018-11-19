package me.jace.learnningspringrestdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jace.learnningspringrestdocs.controller.BookInput;
import me.jace.learnningspringrestdocs.controller.PublisherInput;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookApiDocumentationTest {
    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private RestDocumentationResultHandler documentationHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.documentationHandler = document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.documentationHandler)
                .build();
    }

    @Test
    public void index() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        links(
                                linkWithRel("books").description("The <<resources-books,Books resource>>")),
                        responseFields(
                                subsectionWithPath("_links").description("<<resources-index-access_links,Links>> to other resources"))));
    }

    @Test
    public void bookCreate() throws Exception {
        Map<String, Object> book = new HashMap<>();
        Map<String, Object> publisher = new HashMap<>();
        Map<String, Object> contact = new HashMap<>();
        contact.put("address", "korea");
        contact.put("phone", "+8211112222");
        publisher.put("name", "O'Reilly");
        publisher.put("contact", contact);
        book.put("subject", "test book");
        book.put("description", "this book is good and funny");
        book.put("author", "jace.seo");
        book.put("publisher", publisher);

        this.mockMvc
                .perform(post("/books")
                        .contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(book)))
                .andExpect(
                        status().isCreated())
                .andDo(this.documentationHandler.document(
                        requestFields(getBookFieldDescription()),
                        requestFields(
                                beneathPath("publisher").withSubsectionId("publisher"), getPublisherFieldDescription()),
                        requestFields(
                                beneathPath("publisher.contact").withSubsectionId("contact"), getContactFieldDescription())
                ));
    }

    @Test
    public void bookGet() throws Exception {
        this.mockMvc.perform(get("/books/{id}", 1L).contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        pathParameters(
                                parameterWithName("id").description("The book's ID")),
                        links(
                                halLinks(),
                                linkWithRel("self").description("Link to the self"),
                                linkWithRel("books").description("Link to the books"))));
    }

    @Test
    public void booksAll() throws Exception {
        this.mockMvc.perform(get("/books/all").contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document());
    }

    @Test
    public void booksWithQueryParams() throws Exception {
        this.mockMvc.perform(get("/books?author=jace.seo").contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        requestParameters(
                                parameterWithName("author").description("author parameter"))));
    }

    private FieldDescriptor[] getBookFieldDescription() {
        ConstrainedFields bookInputFields = new ConstrainedFields(BookInput.class);

        return new FieldDescriptor[] {
                bookInputFields.withPath("id").optional().ignored().description("Book's ID"),
                bookInputFields.withPath("subject").description("Title of the book"),
                bookInputFields.withPath("description").description("Description of the book"),
                bookInputFields.withPath("author").description("Author of the book"),
                bookInputFields.subsectionPath("publisher").description("The <<resources-publisher,Publisher request fields>>")
        };
    }

    private FieldDescriptor[] getPublisherFieldDescription() {
        ConstrainedFields publisherFields = new ConstrainedFields(PublisherInput.class);

        return new FieldDescriptor[] {
                publisherFields.withPath("name").description("name of publisher"),
                publisherFields.subsectionPath("contact").description("The <<resources-contact,Contact request fields>>")
        };
    }

    private FieldDescriptor[] getContactFieldDescription() {
        ConstrainedFields contactFields = new ConstrainedFields(PublisherInput.class);
        return new FieldDescriptor[] {
                contactFields.withPath("address").description("address of publisher's"),
                contactFields.withPath("phone").description("phone number of publisher's")
        };
    }


    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }

        private FieldDescriptor subsectionPath(String path) {
            return subsectionWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}
