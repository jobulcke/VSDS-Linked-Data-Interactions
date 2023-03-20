package ldes.client.treenodesupplier.repository.inmemory;

import ldes.client.treenodesupplier.domain.entities.MemberRecord;
import ldes.client.treenodesupplier.domain.valueobject.MemberStatus;
import ldes.client.treenodesupplier.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryMemberRepository implements MemberRepository {

	private List<MemberRecord> unprocessed = new ArrayList<>();
	private List<MemberRecord> processed = new ArrayList<>();

	public Optional<MemberRecord> getUnprocessedTreeMember() {
		if (unprocessed.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(unprocessed.remove(0));
	}

	@Override
	public boolean isProcessed(MemberRecord member) {
		return processed.contains(member);
	}

	@Override
	public void saveTreeMember(MemberRecord treeMember) {
		if (treeMember.getMemberStatus() == MemberStatus.PROCESSED) {
			processed.add(treeMember);
		} else if (treeMember.getMemberStatus() == MemberStatus.UNPROCESSED) {
			unprocessed.add(treeMember);
		}
	}

	@Override
	public void destroyState() {
		processed = new ArrayList<>();
		unprocessed = new ArrayList<>();
	}

}