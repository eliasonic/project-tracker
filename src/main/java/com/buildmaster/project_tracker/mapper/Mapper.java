package com.buildmaster.project_tracker.mapper;

public interface Mapper<A, B> {
    
    B mapToDTO(A a);

    A mapToEntity(B b);

    void update(B b, A a);

}
