CREATE TABLE biz_note_tag_mapping (
    note_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    CONSTRAINT pk_biz_note_tag_mapping PRIMARY KEY (note_id, tag_id),
    CONSTRAINT fk_note_tag_mapping_note FOREIGN KEY (note_id) REFERENCES biz_note(id) ON DELETE CASCADE,
    CONSTRAINT fk_note_tag_mapping_tag FOREIGN KEY (tag_id) REFERENCES biz_tag(id) ON DELETE CASCADE
);

CREATE INDEX idx_note_tag_mapping_note_id ON biz_note_tag_mapping(note_id);
CREATE INDEX idx_note_tag_mapping_tag_id ON biz_note_tag_mapping(tag_id);