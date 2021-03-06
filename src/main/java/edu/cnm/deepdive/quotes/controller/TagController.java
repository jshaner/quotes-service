package edu.cnm.deepdive.quotes.controller;


import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import edu.cnm.deepdive.quotes.model.entity.Tag;
import edu.cnm.deepdive.quotes.service.QuoteRepository;
import edu.cnm.deepdive.quotes.service.TagRepository;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@ExposesResourceFor(Tag.class) // TODO Adjust when copying  to the other controllers.

public class TagController {

  private final QuoteRepository quoteRepository;
  private final TagRepository tagRepository;


  @Autowired
  public TagController(QuoteRepository quoteRepository,
      TagRepository tagRepository) {
    this.quoteRepository = quoteRepository;
    this.tagRepository = tagRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Tag> get() {
    return tagRepository.getAllByOrderByNameAsc();
  }

  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Tag get(@PathVariable long id) {
    return tagRepository.findById(id)
        .orElseThrow(NoSuchElementException::new);
  }


  @GetMapping(value = "/{id:\\d+}/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Quote> getQuotes(@PathVariable long id) {
    return tagRepository.findById(id)
        .map((tag) -> quoteRepository.getAllByTagsContainingOrderByTextAsc(tag))
        .orElseThrow(() -> new NoSuchElementException());
  }
}

