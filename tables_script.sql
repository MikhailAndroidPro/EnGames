-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.Game1 (
  id integer GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  task character varying NOT NULL,
  answer0 character varying,
  answer1 character varying,
  answer2 character varying,
  answer3 character varying,
  correct_answer_id smallint,
  CONSTRAINT Game1_pkey PRIMARY KEY (id)
);
CREATE TABLE public.Game2 (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  word_ru0 character varying NOT NULL,
  word_ru1 character varying,
  word_ru2 character varying,
  word_ru3 character varying,
  word_en0 character varying,
  word_en1 character varying,
  word_en2 character varying,
  word_en3 character varying,
  CONSTRAINT Game2_pkey PRIMARY KEY (id)
);
CREATE TABLE public.Game3 (
  id integer GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  task character varying,
  correct_answer character varying,
  CONSTRAINT Game3_pkey PRIMARY KEY (id)
);
CREATE TABLE public.Game4 (
  id integer GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  answer0 character varying NOT NULL,
  answer1 character varying,
  answer2 character varying,
  answer3 character varying,
  correct_answer_id smallint,
  task text NOT NULL,
  CONSTRAINT Game4_pkey PRIMARY KEY (id)
);
CREATE TABLE public.Game5 (
  id integer GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  task character varying NOT NULL,
  answer0 character varying,
  answer1 character varying,
  answer2 character varying,
  answer3 character varying,
  correct_answer_id smallint,
  CONSTRAINT Game5_pkey PRIMARY KEY (id)
);
CREATE TABLE public.Games (
  id smallint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  description text NOT NULL,
  name text NOT NULL,
  description_ru text NOT NULL,
  name_ru text NOT NULL,
  CONSTRAINT Games_pkey PRIMARY KEY (id)
);
CREATE TABLE public.Gender (
  id smallint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  is_male boolean NOT NULL DEFAULT true,
  name character varying UNIQUE,
  CONSTRAINT Gender_pkey PRIMARY KEY (id, is_male)
);
CREATE TABLE public.Language (
  id smallint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  language_code text NOT NULL,
  CONSTRAINT Language_pkey PRIMARY KEY (id)
);
CREATE TABLE public.Statistic (
  id integer GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  last_entrance text NOT NULL,
  quiz_score integer NOT NULL DEFAULT 0,
  game1_rating smallint NOT NULL DEFAULT '0'::smallint,
  game2_rating smallint NOT NULL DEFAULT '0'::smallint,
  game3_rating smallint NOT NULL DEFAULT '0'::smallint,
  game4_rating smallint NOT NULL DEFAULT '0'::smallint,
  uuid text NOT NULL,
  user_rating integer DEFAULT ((((COALESCE((game1_rating)::integer, 0) + COALESCE((game2_rating)::integer, 0)) + COALESCE((game3_rating)::integer, 0)) + COALESCE((game4_rating)::integer, 0)) + COALESCE(quiz_score, 0)),
  CONSTRAINT Statistic_pkey PRIMARY KEY (id),
  CONSTRAINT Statistic_uuid_fkey FOREIGN KEY (uuid) REFERENCES public.User(uuid)
);
CREATE TABLE public.Theme (
  id smallint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
  is_light_theme boolean NOT NULL DEFAULT false,
  CONSTRAINT Theme_pkey PRIMARY KEY (id)
);
CREATE TABLE public.User (
  username text NOT NULL,
  email character varying,
  photo_link text,
  password_hash integer NOT NULL,
  gender_id smallint NOT NULL DEFAULT '1'::smallint,
  current_language smallint NOT NULL DEFAULT '1'::smallint,
  theme_id smallint NOT NULL DEFAULT '1'::smallint,
  uuid text NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  CONSTRAINT User_pkey PRIMARY KEY (uuid),
  CONSTRAINT User_theme_id_fkey FOREIGN KEY (theme_id) REFERENCES public.Theme(id),
  CONSTRAINT User_gender_id_fkey FOREIGN KEY (gender_id) REFERENCES public.Gender(id),
  CONSTRAINT User_current_language_fkey FOREIGN KEY (current_language) REFERENCES public.Language(id)
);