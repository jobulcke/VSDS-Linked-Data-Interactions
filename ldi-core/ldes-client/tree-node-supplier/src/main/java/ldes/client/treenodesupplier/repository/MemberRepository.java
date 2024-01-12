package ldes.client.treenodesupplier.repository;

import ldes.client.treenodesupplier.domain.entities.MemberRecord;

import java.util.Optional;
import java.util.stream.Stream;

public interface MemberRepository {

	Optional<MemberRecord> getUnprocessedTreeMember();

	boolean isProcessed(MemberRecord member);

	void saveTreeMembers(Stream<MemberRecord> treeMemberStream);

	void saveTreeMember(MemberRecord treeMember);

	void destroyState();
}
