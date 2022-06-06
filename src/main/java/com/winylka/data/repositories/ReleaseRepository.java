package com.winylka.data.repositories;

import com.winylka.data.entities.Release;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

    public List<Release> findReleasesByReleaseDateBetween(Date before, Date after);

    public List<Release> findReleasesByAlbumAlbumId(long albumId);

    public Release findByReleaseId(long id);
}
