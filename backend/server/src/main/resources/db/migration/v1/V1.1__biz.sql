DROP TABLE IF EXISTS "public"."biz_dotfiles";
CREATE TABLE "public"."biz_dotfiles" (
  "id" int8 NOT NULL,
  "create_by" int8 NOT NULL,
  "update_by" int8,
  "create_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6),
  "os_type" int4 NOT NULL,
  "os_structure" text COLLATE "pg_catalog"."default" NOT NULL,
  "def_type" text COLLATE "pg_catalog"."default" NOT NULL,
  "name" text COLLATE "pg_catalog"."default" NOT NULL,
  "value" text COLLATE "pg_catalog"."default" NOT NULL,
  "describtion" text COLLATE "pg_catalog"."default",
  "status" int4 NOT NULL,
  "file_url" text COLLATE "pg_catalog"."default",
  "location" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."biz_dotfiles" OWNER TO "postgres";
COMMENT ON COLUMN "public"."biz_dotfiles"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."biz_dotfiles"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."biz_dotfiles"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."biz_dotfiles"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."biz_dotfiles"."os_type" IS '操作系统';
COMMENT ON COLUMN "public"."biz_dotfiles"."os_structure" IS '系统架构';
COMMENT ON COLUMN "public"."biz_dotfiles"."def_type" IS '定义类型';
COMMENT ON COLUMN "public"."biz_dotfiles"."name" IS '名称';
COMMENT ON COLUMN "public"."biz_dotfiles"."value" IS '值';
COMMENT ON COLUMN "public"."biz_dotfiles"."describtion" IS '注释';
COMMENT ON COLUMN "public"."biz_dotfiles"."status" IS '状态1=启用0=未启用';
COMMENT ON COLUMN "public"."biz_dotfiles"."file_url" IS '文件地址';
COMMENT ON COLUMN "public"."biz_dotfiles"."location" IS '文件位置';
COMMENT ON TABLE "public"."biz_dotfiles" IS '配置文件管理';

-- ----------------------------
-- Records of biz_dotfiles
-- ----------------------------
BEGIN;
INSERT INTO "public"."biz_dotfiles" ("id", "create_by", "update_by", "create_time", "update_time", "os_type", "os_structure", "def_type", "name", "value", "describtion", "status", "file_url", "location") VALUES (1907671562944610304, 1, 1, '2025-04-03 13:48:38.086375', '2025-04-05 19:09:51.72085', 6, '1', '3', 'visual-studio-code', 'oxxxxxxxxxxxx', 'vscode编辑器', 1, NULL, '/Applications/Visual Studio Code.app');
COMMIT;

-- ----------------------------
-- Table structure for biz_mapping
-- ----------------------------
DROP TABLE IF EXISTS "public"."biz_mapping";
CREATE TABLE "public"."biz_mapping" (
  "from_id" int8 NOT NULL,
  "to_id" int8 NOT NULL,
  "mapping_type" text COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "public"."biz_mapping" OWNER TO "postgres";
COMMENT ON TABLE "public"."biz_mapping" IS '多对多关系表';

-- ----------------------------
-- Records of biz_mapping
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for biz_note
-- ----------------------------
DROP TABLE IF EXISTS "public"."biz_note";
CREATE TABLE "public"."biz_note" (
  "id" int8 NOT NULL,
  "create_by" int8,
  "update_by" int8,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "title" text COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default",
  "type" text COLLATE "pg_catalog"."default",
  "parent_id" int8
)
;
ALTER TABLE "public"."biz_note" OWNER TO "postgres";
COMMENT ON COLUMN "public"."biz_note"."id" IS '主键';
COMMENT ON COLUMN "public"."biz_note"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."biz_note"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."biz_note"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."biz_note"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."biz_note"."title" IS '标题';
COMMENT ON COLUMN "public"."biz_note"."content" IS '内容';
COMMENT ON COLUMN "public"."biz_note"."type" IS '类型 @return 笔记类型';
COMMENT ON COLUMN "public"."biz_note"."parent_id" IS '笔记的父节点，表示当前笔记的父笔记。 通过 {@link ManyToOne} 注解与子笔记关联。 @return 父笔记，如果没有父笔记则返回 null';
COMMENT ON TABLE "public"."biz_note" IS '笔记实体类';

-- ----------------------------
-- Records of biz_note
-- ----------------------------
BEGIN;
INSERT INTO "public"."biz_note" ("id", "create_by", "update_by", "create_time", "update_time", "title", "content", "type", "parent_id") VALUES (1111222, 1, 1, '2025-01-20 19:04:29.98746', '2025-01-20 19:04:29.98746', 'test title', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for biz_tag
-- ----------------------------
DROP TABLE IF EXISTS "public"."biz_tag";
CREATE TABLE "public"."biz_tag" (
  "id" int8 NOT NULL,
  "create_by" int8,
  "update_by" int8,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "name" text COLLATE "pg_catalog"."default",
  "description" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."biz_tag" OWNER TO "postgres";
COMMENT ON COLUMN "public"."biz_tag"."id" IS '主键';
COMMENT ON COLUMN "public"."biz_tag"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."biz_tag"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."biz_tag"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."biz_tag"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."biz_tag"."name" IS '标签名称';
COMMENT ON COLUMN "public"."biz_tag"."description" IS '标签描述';
COMMENT ON TABLE "public"."biz_tag" IS '标签实体类，用于管理笔记的标签系统 该实体类映射到数据库表 `biz_tag`';

-- ----------------------------
-- Records of biz_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Primary Key structure for table biz_dotfiles
-- ----------------------------
ALTER TABLE "public"."biz_dotfiles" ADD CONSTRAINT "biz_env_vars_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table biz_mapping
-- ----------------------------
CREATE INDEX "idx_biz_mapping_from_id" ON "public"."biz_mapping" USING btree (
  "from_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_biz_mapping_to_id" ON "public"."biz_mapping" USING btree (
  "to_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_biz_mapping_type" ON "public"."biz_mapping" USING btree (
  "mapping_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table biz_mapping
-- ----------------------------
ALTER TABLE "public"."biz_mapping" ADD CONSTRAINT "pk_biz_mapping" PRIMARY KEY ("from_id", "to_id", "mapping_type");

-- ----------------------------
-- Primary Key structure for table biz_note
-- ----------------------------
ALTER TABLE "public"."biz_note" ADD CONSTRAINT "biz_note_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table biz_tag
-- ----------------------------
ALTER TABLE "public"."biz_tag" ADD CONSTRAINT "biz_tag_pkey" PRIMARY KEY ("id");
