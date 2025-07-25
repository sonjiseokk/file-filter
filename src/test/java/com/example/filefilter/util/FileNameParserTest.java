package com.example.filefilter.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileNameParserTest {
    @Test
    @DisplayName("정상적인 파싱")
    public void 정상적인_파싱() {
        String fileName = "name.txt";
        String name = FileNameParser.getName(fileName);
        String extension = FileNameParser.getExtension(fileName);

        Assertions.assertThat(name).isEqualTo("name.txt");
        Assertions.assertThat(extension).isEqualTo("txt");
    }

    @Test
    @DisplayName("경로 입력 파싱")
    public void 경로_입력_파싱() {
        String fileName = "C://test/test/test/name.txt";
        String name = FileNameParser.getName(fileName);
        String extension = FileNameParser.getExtension(fileName);

        Assertions.assertThat(name).isEqualTo("name.txt");
        Assertions.assertThat(extension).isEqualTo("txt");
    }

    @Test
    @DisplayName("악의적 경로 입력 파싱")
    public void 악의적_경로_입력_파싱() {
        String fileName = "../../../../etc/passwd";
        String name = FileNameParser.getName(fileName);
        String extension = FileNameParser.getExtension(fileName);

        Assertions.assertThat(name).isEqualTo("passwd");
        Assertions.assertThat(extension).isEqualTo("");
    }

    @Test
    @DisplayName("숨김 파일 입력 파싱")
    public void 숨김_파일_입력_파싱() {
        String fileName = ".gitignore";
        String name = FileNameParser.getName(fileName);
        String extension = FileNameParser.getExtension(fileName);

        Assertions.assertThat(name).isEqualTo(".gitignore");
        Assertions.assertThat(extension).isEqualTo("");
    }

    @Test
    @DisplayName("숨김 텍스트 파일 입력 파싱")
    public void 숨김_텍스트_파일_입력_파싱() {
        String fileName = ".gitignore.txt";
        String name = FileNameParser.getName(fileName);
        String extension = FileNameParser.getExtension(fileName);

        Assertions.assertThat(name).isEqualTo(".gitignore.txt");
        Assertions.assertThat(extension).isEqualTo("txt");
    }


}