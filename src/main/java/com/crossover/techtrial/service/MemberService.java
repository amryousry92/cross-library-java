/**
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TopMemberDTO;
import java.time.LocalDateTime;
import java.util.List;
import com.crossover.techtrial.model.Member;

/**
 * RideService for rides.
 *
 * @author crossover
 */
public interface MemberService {

    public Member save(Member member);

    public Member findById(Long memberId);

    public List<Member> findAll();

    public List<TopMemberDTO> findTopMembers(LocalDateTime startTime, LocalDateTime endTime);

    void deleteById(Long id);
}
