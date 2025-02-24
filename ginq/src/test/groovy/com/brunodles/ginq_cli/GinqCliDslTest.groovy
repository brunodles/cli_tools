package com.brunodles.ginq_cli

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import groovy.transform.TupleConstructor
import spock.lang.Specification

class GinqCliDslTest extends Specification {

    private static List<Map<String, ?>> SAMPLE_LIST_OF_MAP =
            [
                    [key: "zelda", name: "Zelda", url: null, expectedPins: 0, pinCount: 0, imagesCount: 93, enabled: false, lastChange: "2022.01 06 20:50:13"],
                    [key: "yugioh", name: "YuGiOh", url: null, expectedPins: 0, pinCount: 0, imagesCount: 4, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "your_name", name: "Your Name", url: null, expectedPins: 0, pinCount: 0, imagesCount: 31, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "x_men", name: "X - Men", url: null, expectedPins: 0, pinCount: 0, imagesCount: 1, enabled: false, lastChange: "2023.05.17 21:03:16"],
                    [key: "we_bare_bears", name: "We Bare Bears", url: null, expectedPins: 0, pinCount: 0, imagesCount: 47, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "wallpaper", name: "Wallpaper", url: null, expectedPins: 0, pinCount: 0, imagesCount: 450, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "video_game", name: "Video Game", url: null, expectedPins: 0, pinCount: 0, imagesCount: 22, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "video", name: "Video", url: null, expectedPins: 0, pinCount: 0, imagesCount: 20, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "trip_ing_skies", name: "Tripping Skies", url: null, expectedPins: 0, pinCount: 0, imagesCount: 5, enabled: false, lastChange: "2023.05.17 21:03:16"],
                    [key: "tower_of_god", name: "Tower Of God", url: null, expectedPins: 0, pinCount: 0, imagesCount: 15, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "topdown", name: "TopDown", url: null, expectedPins: 0, pinCount: 0, imagesCount: 7, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "the_witcher", name: "The Witcher", url: null, expectedPins: 0, pinCount: 0, imagesCount: 67, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "tartarugas_ninjas", name: "Tartarugas Ninjas", url: null, expectedPins: 0, pinCount: 0, imagesCount: 7, enabled: false, lastChange: "2022.01.06 20:50:13"],
                    [key: "tmnt", name: "TMNT", url: null, expectedPins: 0, pinCount: 0, imagesCount: 1, enabled: false, lastChange: "2023.05.17 21:03:16"],
            ]
    private static List<Board> SAMPLE_LIST_OF_BOARD = SAMPLE_LIST_OF_MAP.collect { new Board(it) }
    private static final String EXPECTED_CSV_RESULT = """
key,name,url,expectedPins,pinCount,imagesCount,enabled,lastChange
zelda,Zelda,,0,0,93,false,2022.01 06 20:50:13
yugioh,YuGiOh,,0,0,4,false,2022.01.06 20:50:13
your_name,Your Name,,0,0,31,false,2022.01.06 20:50:13
x_men,X - Men,,0,0,1,false,2023.05.17 21:03:16
we_bare_bears,We Bare Bears,,0,0,47,false,2022.01.06 20:50:13
wallpaper,Wallpaper,,0,0,450,false,2022.01.06 20:50:13
video_game,Video Game,,0,0,22,false,2022.01.06 20:50:13
video,Video,,0,0,20,false,2022.01.06 20:50:13
trip_ing_skies,Tripping Skies,,0,0,5,false,2023.05.17 21:03:16
tower_of_god,Tower Of God,,0,0,15,false,2022.01.06 20:50:13
topdown,TopDown,,0,0,7,false,2022.01.06 20:50:13
the_witcher,The Witcher,,0,0,67,false,2022.01.06 20:50:13
tartarugas_ninjas,Tartarugas Ninjas,,0,0,7,false,2022.01.06 20:50:13
tmnt,TMNT,,0,0,1,false,2023.05.17 21:03:16
        """.trim().stripIndent()
    private static final String EXPECTED_TSV_RESULT = EXPECTED_CSV_RESULT.replaceAll(",", "\t")
    private static final String EXPECTED_JSON_RESULT = "[{\"key\":\"zelda\",\"name\":\"Zelda\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":93,\"enabled\":false,\"lastChange\":\"2022.01 06 20:50:13\"},{\"key\":\"yugioh\",\"name\":\"YuGiOh\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":4,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"your_name\",\"name\":\"Your Name\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":31,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"x_men\",\"name\":\"X - Men\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":1,\"enabled\":false,\"lastChange\":\"2023.05.17 21:03:16\"},{\"key\":\"we_bare_bears\",\"name\":\"We Bare Bears\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":47,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"wallpaper\",\"name\":\"Wallpaper\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":450,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"video_game\",\"name\":\"Video Game\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":22,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"video\",\"name\":\"Video\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":20,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"trip_ing_skies\",\"name\":\"Tripping Skies\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":5,\"enabled\":false,\"lastChange\":\"2023.05.17 21:03:16\"},{\"key\":\"tower_of_god\",\"name\":\"Tower Of God\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":15,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"topdown\",\"name\":\"TopDown\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":7,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"the_witcher\",\"name\":\"The Witcher\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":67,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"tartarugas_ninjas\",\"name\":\"Tartarugas Ninjas\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":7,\"enabled\":false,\"lastChange\":\"2022.01.06 20:50:13\"},{\"key\":\"tmnt\",\"name\":\"TMNT\",\"url\":null,\"expectedPins\":0,\"pinCount\":0,\"imagesCount\":1,\"enabled\":false,\"lastChange\":\"2023.05.17 21:03:16\"}]"
    private static final String EXPECTED_YAML_RESULT = """---
- key: "zelda"
  name: "Zelda"
  url: null
  expectedPins: 0
  pinCount: 0
  imagesCount: 93
  enabled: false
  lastChange: "2022.01 06 20:50:13"
- key: "yugioh"
  name: "YuGiOh"
  url: null
  expectedPins: 0
  pinCount: 0
  imagesCount: 4
  enabled: false
  lastChange: "2022.01.06 20:50:13"
- key: "your_name"
  name: "Your Name"
  url: null
  expectedPins: 0
  pinCount: 0
  imagesCount: 31
  enabled: false
  lastChange: "2022.01.06 20:50:13"
""".stripIndent()

    private def ginqCli = new GinqCliDsl() {
        @Override
        Object run() {
            return null
        }
    }

    private static Object gqAsQueryableCollection(Iterable data = SAMPLE_LIST_OF_MAP) {
        return GQ {
            from board in data
            select board
        }
    }

    private static Object gqAsQueryableCollectionWithSelect(Iterable data = SAMPLE_LIST_OF_MAP) {
        return GQ {
            from board in data
            select board.key, board.name, board.url, board.expectedPins, board.pinCount, board.imagesCount, board.enabled, board.lastChange
        }
    }

    def "CSV, should parse correctly"() {
        when:
        def result = ginqCli.csv(input)

        then:
        assert result == expected

        where:
        name                                               | input                                                            | expected
        "list of map"                                      | SAMPLE_LIST_OF_MAP                                               | EXPECTED_CSV_RESULT
        "list of Object"                                   | SAMPLE_LIST_OF_BOARD                                             | EXPECTED_CSV_RESULT
        "QueryableCollection"                              | gqAsQueryableCollection()                                        | EXPECTED_CSV_RESULT
        "QueryableCollection toList"                       | gqAsQueryableCollection().toList()                               | EXPECTED_CSV_RESULT
        "QueryableCollection of Object"                    | gqAsQueryableCollection(SAMPLE_LIST_OF_BOARD)                    | EXPECTED_CSV_RESULT
        "QueryableCollection of Object toList"             | gqAsQueryableCollection(SAMPLE_LIST_OF_BOARD).toList()           | EXPECTED_CSV_RESULT
        "QueryableCollection with Select of Object"        | gqAsQueryableCollectionWithSelect(SAMPLE_LIST_OF_BOARD)          | EXPECTED_CSV_RESULT
        "QueryableCollection with Select of Object toList" | gqAsQueryableCollectionWithSelect(SAMPLE_LIST_OF_BOARD).toList() | EXPECTED_CSV_RESULT
    }

    def "TSV, should parse correctly"() {
        when:
        def result = ginqCli.tsv(input)

        then:
        assert result == expected

        where:
        name                                               | input                                                            | expected
        "list of map"                                      | SAMPLE_LIST_OF_MAP                                               | EXPECTED_TSV_RESULT
        "list of Object"                                   | SAMPLE_LIST_OF_BOARD                                             | EXPECTED_TSV_RESULT
        "QueryableCollection"                              | gqAsQueryableCollection()                                        | EXPECTED_TSV_RESULT
        "QueryableCollection toList"                       | gqAsQueryableCollection().toList()                               | EXPECTED_TSV_RESULT
        "QueryableCollection of Object"                    | gqAsQueryableCollection(SAMPLE_LIST_OF_BOARD)                    | EXPECTED_TSV_RESULT
        "QueryableCollection of Object toList"             | gqAsQueryableCollection(SAMPLE_LIST_OF_BOARD).toList()           | EXPECTED_TSV_RESULT
        "QueryableCollection with Select of Object"        | gqAsQueryableCollectionWithSelect(SAMPLE_LIST_OF_BOARD)          | EXPECTED_TSV_RESULT
        "QueryableCollection with Select of Object toList" | gqAsQueryableCollectionWithSelect(SAMPLE_LIST_OF_BOARD).toList() | EXPECTED_TSV_RESULT
    }

    def "Json, should parse correctly"() {
        when:
        def result = ginqCli.json(input)

        then:
        assert result == expected

        where:
        name                                               | input                                                            | expected
        "list of map"                                      | SAMPLE_LIST_OF_MAP                                               | EXPECTED_JSON_RESULT
        "list of Object"                                   | SAMPLE_LIST_OF_BOARD                                             | EXPECTED_JSON_RESULT
        "QueryableCollection"                              | gqAsQueryableCollection()                                        | EXPECTED_JSON_RESULT
        "QueryableCollection toList"                       | gqAsQueryableCollection().toList()                               | EXPECTED_JSON_RESULT
        "QueryableCollection of Object"                    | gqAsQueryableCollection(SAMPLE_LIST_OF_BOARD)                    | EXPECTED_JSON_RESULT
        "QueryableCollection of Object toList"             | gqAsQueryableCollection(SAMPLE_LIST_OF_BOARD).toList()           | EXPECTED_JSON_RESULT
        "QueryableCollection with Select of Object"        | gqAsQueryableCollectionWithSelect(SAMPLE_LIST_OF_BOARD)          | EXPECTED_JSON_RESULT
        "QueryableCollection with Select of Object toList" | gqAsQueryableCollectionWithSelect(SAMPLE_LIST_OF_BOARD).toList() | EXPECTED_JSON_RESULT
    }

    def "Yaml, should parse correctly"() {
        when:
        def result = ginqCli.yaml(input)

        then:
        assert result == expected

        where:
        name                                               | input                                                                          | expected
        "list of map"                                      | SAMPLE_LIST_OF_MAP.subList(0, 3)                                               | EXPECTED_YAML_RESULT
        "list of object"                                   | SAMPLE_LIST_OF_BOARD.subList(0, 3)                                             | EXPECTED_YAML_RESULT
        "QueryableCollection"                              | gqAsQueryableCollection(SAMPLE_LIST_OF_MAP.subList(0, 3))                      | EXPECTED_YAML_RESULT
        "QueryableCollection toList"                       | gqAsQueryableCollection(SAMPLE_LIST_OF_MAP.subList(0, 3)).toList()             | EXPECTED_YAML_RESULT
        "QueryableCollection of Object"                    | gqAsQueryableCollection(SAMPLE_LIST_OF_BOARD.subList(0, 3))                    | EXPECTED_YAML_RESULT
        "QueryableCollection of Object toList"             | gqAsQueryableCollection(SAMPLE_LIST_OF_BOARD.subList(0, 3)).toList()           | EXPECTED_YAML_RESULT
        "QueryableCollection with Select of Object"        | gqAsQueryableCollectionWithSelect(SAMPLE_LIST_OF_BOARD.subList(0, 3))          | EXPECTED_YAML_RESULT
        "QueryableCollection with Select of Object toList" | gqAsQueryableCollectionWithSelect(SAMPLE_LIST_OF_BOARD.subList(0, 3)).toList() | EXPECTED_YAML_RESULT
    }

    @Immutable
    @TupleConstructor
    @CompileStatic
    private static class Board {
        String key
        String name
        String url
        Integer expectedPins
        Integer pinCount
        Integer imagesCount
        Boolean enabled
        String lastChange
    }
}
