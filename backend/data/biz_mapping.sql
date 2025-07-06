CREATE TABLE biz_mapping (
    from_id BIGINT NOT NULL,
    to_id BIGINT NOT NULL,
    mapping_type VARCHAR(50) NOT NULL,
    CONSTRAINT pk_biz_mapping PRIMARY KEY (from_id, to_id, mapping_type)
);

CREATE INDEX idx_biz_mapping_from_id ON biz_mapping(from_id);
CREATE INDEX idx_biz_mapping_to_id ON biz_mapping(to_id);
CREATE INDEX idx_biz_mapping_type ON biz_mapping(mapping_type);