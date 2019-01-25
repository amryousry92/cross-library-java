/**
 *
 */
package com.crossover.techtrial.repositories;

import com.crossover.techtrial.dto.TopMemberDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import com.crossover.techtrial.model.Member;

/**
 * Person repository for basic operations on Person entity.
 *
 * @author crossover
 */
@RestResource(exported = false)
public interface MemberRepository extends PagingAndSortingRepository<Member, Long> {

    String TOP_MEMBERS_QUERY =
        "Select member.name, Count(transaction.member_id) as total "
            + "From member left join transaction on member.id = transaction.member_id  "
            + "Where transaction.date_of_issue BETWEEN (:startTime) AND (:endTime) "
            + "and transaction.date_of_return BETWEEN (:startTime) AND (:endTime) "
            + "group by transaction.member_id "
            + "order by total DESC "
            + "Limit 5";

    Optional<Member> findById(Long id);

    List<Member> findAll();

    @Query(value = TOP_MEMBERS_QUERY, nativeQuery = true)
    List<TopMemberDTO> findTopMembers(@Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime);
}
