package cl.bch.cloud.restclients;

import cl.bch.cloud.dtos.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name="JsonPlaceHolderClient", url="${rest.endpoints.jsonplaceholder.url}")
public interface JsonPlaceHolderClient {

    @RequestMapping(method = RequestMethod.GET, value = "/posts", produces = "application/json")
    List<PostDTO> getAll();

    @RequestMapping(method = RequestMethod.GET, value = "/posts/{postId}", produces = "application/json")
    PostDTO getPostById(@PathVariable("postId") Long postId);

}