package whackamr.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import whackamr.data.dto.MergeRequestDto;
import whackamr.data.repository.MergeRequestRepository;

@Transactional
@RestController
@AllArgsConstructor
@RequestMapping("/api/mergeRequests")
public class MergeRequestsController
{
    private ModelMapper mapper;
    private MergeRequestRepository mergeRequestRepository;

    @GetMapping
    public List<MergeRequestDto> getAllMergeRequests()
    {
        var mergeRequests = new ArrayList<MergeRequestDto>();
        var entities = mergeRequestRepository.findAll();

        entities.forEach(mergeReqest -> {
            mergeRequests.add(mapper.map(mergeRequestRepository, MergeRequestDto.class));
        });

        return mergeRequests;
    }
}
