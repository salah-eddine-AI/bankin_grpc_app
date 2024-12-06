package ma.projet.grpc.controllers;

import io.grpc.stub.StreamObserver;
import ma.projet.grpc.Entitys.Compte;
import ma.projet.grpc.Repositorys.CompteRepository;
import ma.projet.grpc.stubs.*;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class CompteServiceImpl extends CompteServiceGrpc.CompteServiceImplBase {

    @Autowired
    private CompteRepository compteRepository;

    @Override
    public void allComptes(GetAllComptesRequest request, StreamObserver<GetAllComptesResponse> responseObserver) {
        List<Compte> comptes = compteRepository.findAll();
        GetAllComptesResponse.Builder responseBuilder = GetAllComptesResponse.newBuilder();

        comptes.forEach(compte -> {
            responseBuilder.addComptes(
                    ma.projet.grpc.stubs.Compte.newBuilder()
                            .setId(compte.getId())
                            .setSolde(compte.getSolde())
                            .setDateCreation(compte.getDateCreation())
                            .setType(compte.getType())
                            .build()
            );
        });

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void compteById(GetCompteByIdRequest request, StreamObserver<GetCompteByIdResponse> responseObserver) {
        Optional<Compte> compteOptional = compteRepository.findById(request.getId());

        if (compteOptional.isPresent()) {
            Compte compte = compteOptional.get();
            responseObserver.onNext(
                    GetCompteByIdResponse.newBuilder()
                            .setCompte(ma.projet.grpc.stubs.Compte.newBuilder()
                                    .setId(compte.getId())
                                    .setSolde(compte.getSolde())
                                    .setDateCreation(compte.getDateCreation())
                                    .setType(compte.getType())
                                    .build())
                            .build()
            );
        } else {
            responseObserver.onError(new Throwable("Compte non trouvé"));
        }

        responseObserver.onCompleted();
    }

    @Override
    public void totalSolde(GetTotalSoldeRequest request, StreamObserver<GetTotalSoldeResponse> responseObserver) {
        List<Compte> comptes = compteRepository.findAll();
        int count = comptes.size();
        double sum = comptes.stream().mapToDouble(Compte::getSolde).sum(); // Use mapToDouble for float values
        float average = count > 0 ? (float) (sum / count) : 0; // Explicit cast to float for average

        SoldeStats stats = SoldeStats.newBuilder()
                .setCount(count)
                .setSum((float) sum) // Explicit cast to float for sum
                .setAverage(average)
                .build();

        responseObserver.onNext(GetTotalSoldeResponse.newBuilder().setStats(stats).build());
        responseObserver.onCompleted();
    }


    @Override
    public void saveCompte(SaveCompteRequest request, StreamObserver<SaveCompteResponse> responseObserver) {
        CompteRequest compteReq = request.getCompte();

        Compte compte = new Compte();
        compte.setSolde(compteReq.getSolde());
        compte.setDateCreation(compteReq.getDateCreation());
        compte.setType(compteReq.getType());

        Compte savedCompte = compteRepository.save(compte);

        responseObserver.onNext(
                SaveCompteResponse.newBuilder()
                        .setCompte(ma.projet.grpc.stubs.Compte.newBuilder()
                                .setId(savedCompte.getId())
                                .setSolde(savedCompte.getSolde())
                                .setDateCreation(savedCompte.getDateCreation())
                                .setType(savedCompte.getType())
                                .build())
                        .build()
        );

        responseObserver.onCompleted();
    }


    @Override
    public void deleteCompteById(DeleteCompteByIdRequest request, StreamObserver<DeleteCompteByIdResponse> responseObserver) {
        String compteId = request.getId();

        if (compteRepository.existsById(compteId)) {
            compteRepository.deleteById(compteId);
            DeleteCompteByIdResponse response = DeleteCompteByIdResponse.newBuilder()
                    .setMessage("Compte with ID " + compteId + " has been deleted successfully.")
                    .build();
            responseObserver.onNext(response);
        } else {
            responseObserver.onError(new Throwable("Compte with ID " + compteId + " not found."));
        }

        responseObserver.onCompleted();
    }


}
