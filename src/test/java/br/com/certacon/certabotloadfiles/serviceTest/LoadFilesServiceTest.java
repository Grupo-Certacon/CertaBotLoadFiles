package br.com.certacon.certabotloadfiles.serviceTest;


import br.com.certacon.certabotloadfiles.dto.LoadFilesDto;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.service.LoadFilesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
public class LoadFilesServiceTest {
    @MockBean
    LoadFilesService loadFilesService;
    @MockBean
    LoadFilesRepository loadFilesRepository;

    LoadFilesModel loadFilesModel;

    @Test
    @DisplayName("chamar o metodo createFolder Quando Retornar com Sucesso")
    void shouldCallFolderServiceWhenCreateFolderReturnWithSuccess() {
        //Given
        LoadFilesModel expected = new LoadFilesModel();
        //When
        BDDMockito.when(loadFilesRepository.save(any())).thenReturn(expected);
        LoadFilesModel actual = loadFilesService.create(expected);
        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("chamar o metodo createFolder Quando Retornar com Falha")
    void shouldCallLoadFilesServiceWhenCreateFolderReturnWithFail() {
        //Given
        Exception expected = new RuntimeException("Objeto não pode ser nulo");
        //When/Then
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(null);
        Exception actual = assertThrows(RuntimeException.class, () -> loadFilesService.create(null));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    @DisplayName("chamar o metodo getOneFolder Quando retornar com Sucesso")
    void shouldCallLoadFilesServiceWhenGetOneFolderReturnWithSuccess() {
        //Given
        Optional<LoadFilesModel> expected = Optional.of(LoadFilesModel.builder()
                .id(UUID.randomUUID())
                .yearFolder("2021")
                .cnpjFolder("123.2.14.111.2")
                .serverFolder("162.345.0.13")
                .build());
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(expected);
        Optional<LoadFilesModel> actual = loadFilesService.getOneFolder(expected.get().getId());
        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("chamar o metodo getOneFolder Quando retornar com Falha")
    void shouldCallLoadFilesServiceWhenGetOneFolderReturnWithFail() {
        //Given
        Exception expected = new RuntimeException("Pasta não existe");
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(null);
        Exception actual = assertThrows(RuntimeException.class, () -> loadFilesService.getOneFolder(null));
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    @DisplayName("chamar o metodo getAllFolders Quando retornar com sucesso")
    void shouldCallLoadFilesServiceWhenGetAllFoldersReturnWithSuccess() {
        //Given
        List<LoadFilesModel> expected = new ArrayList<>();
        //When
        BDDMockito.when(loadFilesRepository.findAll()).thenReturn(expected);
        List<LoadFilesModel> actual = loadFilesService.getAllFolders();
        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("chamar o metodo getAllFolders Quando retornar com Falha")
    void shouldCallLoadFilesServiceWhenGetAllFoldersReturnWithFail() {
        //Given
        Exception expected = new RuntimeException("Pasta(s) não encontradas");
        //When
        BDDMockito.when(loadFilesRepository.findAll()).thenReturn(null);
        Exception actual = assertThrows(RuntimeException.class, () -> loadFilesService.getAllFolders());
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    @DisplayName("chamar o metodo UpdateFolder quando Retornar com Sucesso")
    void shouldCallLoadFilesServiceWhenUpdateFolderReturnWithSuccess() {
        //Given
        LoadFilesDto loadFilesDto = LoadFilesDto.builder()
                .id(loadFilesModel.getId())
                .updatedAt(loadFilesModel.getCreatedAt())
                .path(loadFilesModel.getPath())
                .serverFolder(loadFilesModel.getServerFolder())
                .cnpjFolder(loadFilesModel.getCnpjFolder())
                .yearFolder(loadFilesModel.getYearFolder())
                .build();
        LoadFilesModel expected = new LoadFilesModel();
        Optional<LoadFilesModel> optionalModel = Optional.of(expected);
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(optionalModel);
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(expected);
        LoadFilesModel actual = loadFilesService.updateFolder(loadFilesDto);
        //Then
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    @DisplayName("chamar o metodo UpdateFolder quando Retornar com Falha")
    void shouldCallLoadFilesServiceWhenUpdateFolderReturnWithFail() {
        //Given
        Exception expected = new RuntimeException("Folder não encontrado");
        LoadFilesDto loadFilesDto = LoadFilesDto.builder()
                .id(loadFilesModel.getId())
                .updatedAt(loadFilesModel.getCreatedAt())
                .cnpjFolder(loadFilesModel.getCnpjFolder())
                .serverFolder(loadFilesModel.getServerFolder())
                .yearFolder(loadFilesModel.getYearFolder())
                .path(loadFilesModel.getPath())
                .build();
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(null);
        Exception actual = assertThrows(RuntimeException.class, () -> loadFilesService.updateFolder(loadFilesDto));
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    @DisplayName("chamar o metodo deleteFolder quando retornar com verdadeiro")
    void shouldCallLoadFilesServiceWhenDeleteFolderReturnWithTrue() {
        Optional<LoadFilesModel> result = Optional.of(loadFilesModel);
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(result);
        Boolean actual = loadFilesService.deleteFolder(loadFilesModel.getId());
        //Then
        assertTrue(actual);
    }

    @Test
    @DisplayName("chamar o metodo deleteFolder quando retornar com False")
    void shouldCallLoadFilesServiceWhenDeleteFolderReturnWithFalse() {
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Boolean actual = loadFilesService.deleteFolder(loadFilesModel.getId());
        //Then
        assertFalse(actual);
    }

    @BeforeEach
    void setUp() {
        loadFilesService = new LoadFilesService(loadFilesRepository);
        loadFilesModel = new LoadFilesModel();
        loadFilesModel.setId(UUID.randomUUID());
        loadFilesModel.setCreatedAt(new Date());
        loadFilesModel.setServerFolder("192.168.0.62");
        loadFilesModel.setCnpjFolder("1928321231");
        loadFilesModel.setYearFolder("2004");
        loadFilesModel.setPath("/Home/Selenium/Downloads");
        loadFilesModel.setUpdatedAt(new Date());
    }

    @AfterEach
    void tearDown() {
        loadFilesService = null;
    }
}
